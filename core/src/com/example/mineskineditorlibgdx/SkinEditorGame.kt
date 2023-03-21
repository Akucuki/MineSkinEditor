package com.example.mineskineditorlibgdx

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.model.MeshPart
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.*
import com.badlogic.gdx.math.collision.Ray

data class ModelTriangle(
    val v1: Vector3,
    val v2: Vector3,
    val v3: Vector3,
    val uvs: Array<Vector2>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ModelTriangle

        if (v1 != other.v1) return false
        if (v2 != other.v2) return false
        if (v3 != other.v3) return false
        if (!uvs.contentEquals(other.uvs)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = v1.hashCode()
        result = 31 * result + v2.hashCode()
        result = 31 * result + v3.hashCode()
        result = 31 * result + uvs.contentHashCode()
        return result
    }
}

class SkinEditorGame : ApplicationAdapter() {

    private var environment: Environment? = null
    private var cam: Camera? = null
    private var camController: CameraInputController? = null
    private var backgroundSpriteBatch: SpriteBatch? = null
    private var modelBatch: ModelBatch? = null
    private var instance: ModelInstance? = null
    private var assets: AssetManager? = null
    private var backgroundTexture: Texture? = null
    private val debugColor = Color.RED
    private var areResourcesLoading: Boolean = true

    private lateinit var textureDebugSpriteBatch: SpriteBatch
    private lateinit var shapeDebugRenderer: ShapeRenderer
    private val rayFromWorld = Vector3()
    private val rayToWorld = Vector3()

    override fun create() {
        assets = AssetManager()
        modelBatch = ModelBatch()
        textureDebugSpriteBatch = SpriteBatch()
        shapeDebugRenderer = ShapeRenderer()
        backgroundSpriteBatch = SpriteBatch()
        environment = Environment()
        environment?.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        // Left and right
        environment?.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
        environment?.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, -0.8f, 0.2f))

        // Facing and back
        environment?.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -0.8f, -1f))
        environment?.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -0.8f, 1f))

        // Bottom
        environment?.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, 1f, 0f))

        cam = PerspectiveCamera(80f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam?.position?.set(5f, 15f, 30f)
        cam?.lookAt(0f, 15f, 0f)
        cam?.near = 0.1f
        cam?.far = 100f
        cam?.update()

        camController = CameraInputController(cam)
        Gdx.input.inputProcessor = camController

        val parameter = TextureLoader.TextureParameter()
        parameter.minFilter = Texture.TextureFilter.Nearest
        parameter.magFilter = Texture.TextureFilter.Nearest
        parameter.genMipMaps = false

        assets?.load("character_custom.g3db", Model::class.java)
        assets?.load("texture_steve.png", Texture::class.java, parameter)
        assets?.load("bg_main.png", Texture::class.java)
    }

    private fun finishResourcesLoading() {
        val characterModel = assets!!.get("character_custom.g3db", Model::class.java)
        val modelTexture = assets!!.get("texture_steve.png", Texture::class.java)
        backgroundTexture = assets!!.get("bg_main.png", Texture::class.java)

        instance = ModelInstance(characterModel)
        instance?.materials?.first()?.set(TextureAttribute.createDiffuse(modelTexture))

        areResourcesLoading = false
    }


    override fun render() {
        if (areResourcesLoading && assets?.update() == true) finishResourcesLoading()

        camController?.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        backgroundTexture?.let { texture ->
            backgroundSpriteBatch?.begin()
            backgroundSpriteBatch?.draw(
                texture,
                0f,
                0f,
                Gdx.graphics.width.toFloat(),
                Gdx.graphics.height.toFloat()
            )
            backgroundSpriteBatch?.end()
        }

        modelBatch?.begin(cam)
        instance?.let { modelBatch?.render(it, environment) }
        modelBatch?.end()

        if (Gdx.input.justTouched()) {
            val ray = cam!!.getPickRay(
                Gdx.input.x.toFloat(),
                Gdx.input.y.toFloat(),
                0f,
                0f,
                Gdx.graphics.width.toFloat(),
                Gdx.graphics.height.toFloat()
            )
            rayFromWorld.set(ray.origin.sub(0f, .01f, 0f))
            rayToWorld.set(ray.direction).scl(100f).add(rayFromWorld)

            // DEBUG
            var meshPartsIteratedThroughCount = 0
            var meshesIteratedThroughCount = 0
            var intersectionsFoundCount = 0

            instance!!.model.meshParts.forEach { meshPart ->
                // DEBUG
                meshPartsIteratedThroughCount++

                meshPart.mesh.let { mesh ->
                    // DEBUG
                    meshesIteratedThroughCount++

                    val vertexSize = mesh.vertexSize / 4
                    val vertices = FloatArray(mesh.numVertices * vertexSize)
                    mesh.getVertices(vertices)
                    val indices = ShortArray(mesh.numIndices)
                    mesh.getIndices(indices)

                    val triangles = getModelTriangles(indices, meshPart, vertices, mesh)

                    val intersectionTriangles = triangles.filter {
                        intersectRayTriangle(
                            ray,
                            it.v1,
                            it.v2,
                            it.v3,
                            null
                        )
                    }
                    val nearestIntersectedTriangle = intersectionTriangles.minByOrNull {
                        cam!!.position.dst(getTriangleCentroid(it))
                    }

                    if (nearestIntersectedTriangle != null) {
                        // DEBUG
                        intersectionsFoundCount++

                        val intersectionPoint = Vector3()
                        intersectRayTriangle(
                            ray,
                            nearestIntersectedTriangle.v1,
                            nearestIntersectedTriangle.v2,
                            nearestIntersectedTriangle.v3,
                            intersectionPoint
                        )

                        // DEBUG
                        Gdx.app.log(
                            "vitalik",
                            "Intersection triangles count: ${intersectionTriangles.size}"
                        )
                        Gdx.app.log(
                            "vitalik",
                            "Nearest intersected triangle: $nearestIntersectedTriangle"
                        )
                        Gdx.app.log(
                            "vitalik",
                            "Intersection point: $intersectionPoint"
                        )
                        val instanceTextureAttribute =
                            instance!!.materials.first()
                                .get(TextureAttribute.Diffuse) as TextureAttribute
                        val texture = instanceTextureAttribute.textureDescription.texture
                        val uv = getIntersectionUV(intersectionPoint, nearestIntersectedTriangle)
                        val textureX = (uv.x * texture.width).toInt()
                        val textureY = (uv.y * texture.height).toInt()

                        // DEBUG
                        Gdx.app.log("vitalik", "Texture coords: x: $textureX, y: $textureY")

                        if (!texture.textureData.isPrepared) texture.textureData.prepare()
                        val pixmap = texture.textureData.consumePixmap()
                        pixmap.drawPixel(textureX, textureY, Color.rgba8888(debugColor))
                        val newTexture = Texture(pixmap)
                        instance!!.materials.first().set(TextureAttribute.createDiffuse(newTexture))
                    }
                }
            }

            // DEBUG
            Gdx.app.log("vitalik", "MeshParts iterated through: $meshPartsIteratedThroughCount")
            Gdx.app.log("vitalik", "Meshes iterated through: $meshesIteratedThroughCount")
            Gdx.app.log("vitalik", "Intersections found count: $intersectionsFoundCount")
        }
    }


    override fun dispose() {
        modelBatch?.dispose()
        backgroundSpriteBatch?.dispose()
        assets?.dispose()
    }
}

private fun getModelTriangles(
    indices: ShortArray,
    meshPart: MeshPart,
    vertices: FloatArray,
    mesh: Mesh
): List<ModelTriangle> {
    val triangles = mutableListOf<ModelTriangle>()
    for (i in indices.indices step 3) {
        val stride = meshPart.mesh.vertexSize / 4
        val vertexIndex1 = indices[i].toInt() * stride
        val vertexIndex2 = indices[i + 1].toInt() * stride
        val vertexIndex3 = indices[i + 2].toInt() * stride

        val vertex1 = Vector3(
            vertices[vertexIndex1],
            vertices[vertexIndex1 + 1],
            vertices[vertexIndex1 + 2]
        )
        val vertex2 = Vector3(
            vertices[vertexIndex2],
            vertices[vertexIndex2 + 1],
            vertices[vertexIndex2 + 2]
        )
        val vertex3 = Vector3(
            vertices[vertexIndex3],
            vertices[vertexIndex3 + 1],
            vertices[vertexIndex3 + 2]
        )

        val vertexAttributes = mesh.vertexAttributes
        val uvOffset =
            vertexAttributes.findByUsage(VertexAttributes.Usage.TextureCoordinates).offset / 4

        val uv1 = Vector2(
            vertices[vertexIndex1 + uvOffset],
            vertices[vertexIndex1 + uvOffset + 1]
        )
        val uv2 = Vector2(
            vertices[vertexIndex2 + uvOffset],
            vertices[vertexIndex2 + uvOffset + 1]
        )
        val uv3 = Vector2(
            vertices[vertexIndex3 + uvOffset],
            vertices[vertexIndex3 + uvOffset + 1]
        )

        val uvs = arrayOf(uv1, uv2, uv3)

        val triangle = ModelTriangle(vertex1, vertex2, vertex3, uvs)
        triangles.add(triangle)
    }
    return triangles
}

private fun calculateBarycoord3D(
    point: Vector3,
    a: Vector3,
    b: Vector3,
    c: Vector3
): Vector3 {
    val v0 = Vector3().set(b).sub(a)
    val v1 = Vector3().set(c).sub(a)
    val v2 = Vector3().set(point).sub(a)

    val d00 = v0.dot(v0)
    val d01 = v0.dot(v1)
    val d11 = v1.dot(v1)
    val d20 = v2.dot(v0)
    val d21 = v2.dot(v1)

    val denom = d00 * d11 - d01 * d01
    val barycentricOut = Vector3()
    barycentricOut.x = (d11 * d20 - d01 * d21) / denom
    barycentricOut.y = (d00 * d21 - d01 * d20) / denom
    barycentricOut.z = 1.0f - barycentricOut.x - barycentricOut.y

    return barycentricOut
}

private fun getTriangleCentroid(triangle: ModelTriangle): Vector3 {
    val centroid = Vector3()
    centroid.set(triangle.v1)
    centroid.add(triangle.v2)
    centroid.add(triangle.v3)
    centroid.scl(1f / 3f)
    return centroid
}

private fun getIntersectionUV(
    intersectionPoint: Vector3,
    triangle: ModelTriangle
): Vector2 {
    val barycentricCoords = calculateBarycoord3D(
        intersectionPoint,
        triangle.v1,
        triangle.v2,
        triangle.v3
    )

    require(
        GeometryUtils.barycoordInsideTriangle(
            Vector2(
                barycentricCoords.x,
                barycentricCoords.y
            )
        )
    ) {
        "Barycentric coordinates are outside the triangle!"
    }

    val uv1 = triangle.uvs[0]
    val uv2 = triangle.uvs[1]
    val uv3 = triangle.uvs[2]

    return Vector2(
        barycentricCoords.z * uv1.x + barycentricCoords.x * uv2.x + barycentricCoords.y * uv3.x,
        barycentricCoords.z * uv1.y + barycentricCoords.x * uv2.y + barycentricCoords.y * uv3.y
    )
}

private fun drawVertices(
    vertices: FloatArray,
    vertexSize: Int,
    shapeRenderer: ShapeRenderer,
    color: Color
) {
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
    shapeRenderer.color = color
    for (i in vertices.indices step vertexSize) {
        val x = vertices[i]
        val y = vertices[i + 1]
        val z = vertices[i + 2]
        shapeRenderer.box(x, y, z, .4f, .4f, .4f)
    }
    shapeRenderer.end()
}

private fun intersectRayTriangle(
    ray: Ray,
    t1: Vector3,
    t2: Vector3,
    t3: Vector3,
    intersection: Vector3?
): Boolean {
    val v0 = Vector3()
    val v1 = Vector3()
    val v2 = Vector3()
    val p = Plane(Vector3(), 0f)
    val i = Vector3()

    val edge1 = v0.set(t2).sub(t1)
    val edge2 = v1.set(t3).sub(t1)

    val pvec = v2.set(ray.direction).crs(edge2)
    var det = edge1.dot(pvec)
    if (MathUtils.isZero(det)) {
        p.set(t1, t2, t3)
        if (p.testPoint(ray.origin) == Plane.PlaneSide.OnPlane && safeIsPointInTriangle(
                ray.origin,
                t1,
                t2,
                t3
            )
        ) {
            intersection?.set(ray.origin)
            return true
        }
        return false
    }

    det = 1f / det
    val tvec = i.set(ray.origin).sub(t1)
    val u = tvec.dot(pvec) * det
    if (u < 0f || u > 1f) return false

    val qvec = tvec.crs(edge1)
    val v = ray.direction.dot(qvec) * det
    if (v < 0f || u + v > 1f) return false

    val t = edge2.dot(qvec) * det
    if (t < 0f) return false

    if (intersection != null) {
        if (t <= MathUtils.FLOAT_ROUNDING_ERROR) {
            intersection.set(ray.origin)
        } else {
            ray.getEndPoint(intersection, t)
        }
    }
    return true
}

private fun safeIsPointInTriangle(point: Vector3, t1: Vector3, t2: Vector3, t3: Vector3): Boolean {
    val v0 = Vector3()
    val v1 = Vector3()
    val v2 = Vector3()

    v0.set(t1).sub(point)
    v1.set(t2).sub(point)
    v2.set(t3).sub(point)

    v1.crs(v2)
    v2.crs(v0)

    if (v1.dot(v2) < 0f) return false
    v0.crs(v2.set(t2).sub(point))
    return v1.dot(v0) >= 0f
}
