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
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
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

    private var modelBatch: ModelBatch? = null
    private lateinit var environment: Environment
    private lateinit var cam: Camera
    private var instance: ModelInstance? = null
    private lateinit var camController: CameraInputController
    private lateinit var assets: AssetManager
    private var loading: Boolean = false
    private var backgroundSpriteBatch: SpriteBatch? = null
    private var textureDebugSpriteBatch: SpriteBatch? = null
    private var backgroundTexture: Texture? = null

    override fun create() {
        backgroundSpriteBatch = SpriteBatch()
        textureDebugSpriteBatch = SpriteBatch()
        modelBatch = ModelBatch()
        environment = Environment()
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        // Left and right
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, -0.8f, 0.2f))

        // Facing and back
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -0.8f, -1f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -0.8f, 1f))

        // Bottom
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, 1f, 0f))

        backgroundTexture = Texture("bg_main.png")

        cam = PerspectiveCamera(50f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.position.set(0f, 0f, -5f)
        cam.lookAt(0f, 0f, 0f)
        cam.near = 1f
        cam.far = 40f
        cam.update()

        camController = CameraInputController(cam)
        Gdx.input.inputProcessor = camController

        assets = AssetManager()

        val parameter = TextureLoader.TextureParameter()
        parameter.minFilter = Texture.TextureFilter.Nearest
        parameter.magFilter = Texture.TextureFilter.Nearest
        parameter.genMipMaps = false


//        assets.load("Grass_Block.g3dj", Model::class.java)
//        assets.load("Grass_Block_TEX-large.png", Texture::class.java, parameter)
        assets.load("fixed_creeper.g3db", Model::class.java)
        assets.load("creeper.png", Texture::class.java, parameter)

        loading = true
    }

    private fun doneLoading() {
//        val skinModel = assets.get("Grass_Block.g3dj", Model::class.java)
//        val texture = assets.get("Grass_Block_TEX-large.png", Texture::class.java)
        val skinModel = assets.get("fixed_creeper.g3db", Model::class.java)
        val texture = assets.get("creeper.png", Texture::class.java)

        instance = ModelInstance(skinModel)

        instance!!.materials.first().set(TextureAttribute.createDiffuse(texture))
        loading = false
    }

    override fun render() {
        if (loading && assets.update()) doneLoading()
        camController.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        backgroundSpriteBatch?.begin()
        backgroundSpriteBatch?.draw(
            backgroundTexture,
            0f,
            0f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        )
        backgroundSpriteBatch?.end()

        modelBatch?.begin(cam)
        instance?.let { modelBatch?.render(it, environment) }
        modelBatch?.end()

        val instanceTextureAttribute =
            instance?.materials?.first()?.get(TextureAttribute.Diffuse) as? TextureAttribute
        instanceTextureAttribute?.let { textureAttribute ->
            textureDebugSpriteBatch?.begin()
            textureDebugSpriteBatch?.draw(
                textureAttribute.textureDescription.texture,
                0f,
                0f,
                textureAttribute.textureDescription.texture.width.toFloat() * 5,
                textureAttribute.textureDescription.texture.height.toFloat() * 5
            )
            textureDebugSpriteBatch?.end()
        }

        if (Gdx.input.justTouched()) {
            val ray = cam.getPickRay(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())

            val meshPart = instance!!.model.meshParts[0]

            val numVertices = meshPart.mesh.numVertices
            val vertexSize = meshPart.mesh.vertexSize / 4
            val vertices = FloatArray(numVertices * vertexSize)
            meshPart.mesh.getVertices(0, vertices.size, vertices)

            val indices = ShortArray(meshPart.mesh.numIndices)
            meshPart.mesh.getIndices(indices)

            Gdx.app.log("vitalik", "Vertices: ${vertices.asList()}")
            Gdx.app.log("vitalik", "Vertex size: $vertexSize")
            Gdx.app.log("vitalik", "Num vertices: $numVertices")
            Gdx.app.log("vitalik", "Indices: ${indices.asList()}")


            val triangles = mutableListOf<ModelTriangle>()

//            val tmp1 = Vector3()
//            val tmp2 = Vector3()
//            val tmp3 = Vector3()
//
//            val intersectionPoint = Vector3()
//            val best = Vector3()
//
//            var minDist = Float.MAX_VALUE
//            var hit = false
//
//            if (indices.size % 3 != 0) throw RuntimeException("triangle list size is not a multiple of 3")
//
//            for (i in indices.indices step 3) {
//                val i1 = indices[i].toInt() * vertexSize
//                val i2 = indices[i + 1].toInt() * vertexSize
//                val i3 = indices[i + 2].toInt() * vertexSize
//
//                val isIntersectionFound = Intersector.intersectRayTriangle(
//                    ray,
//                    tmp1.set(vertices[i], vertices[i + 1], vertices[i + 2]),
//                    tmp2.set(vertices[i + 3], vertices[i + 4], vertices[i + 5]),
//                    tmp3.set(vertices[i + 6], vertices[i + 7], vertices[i + 8]),
//                    intersectionPoint
//                )
//
//                if (isIntersectionFound) {
//                    val dist = ray.origin.dst2(intersectionPoint)
//                    if (dist < minDist) {
//                        minDist = dist
//                        best.set(intersectionPoint)
//                        hit = true
//                    }
//                }
//            }
//
//            if (hit) {
//                intersectionPoint.set(best)
//                Gdx.app.log("vitalik", "Hit: $best")
//            }


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

                val vertexAttributes = meshPart.mesh.vertexAttributes
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

            Gdx.app.log("vitalik", "Triangles: $triangles")
            Gdx.app.log("vitalik", "Triangles size: ${triangles.size}")

            val intersectionPoint = Vector3()
            val intersectionTriangle = triangles.find {
                Intersector.intersectRayTriangle(
                    ray,
                    it.v1,
                    it.v2,
                    it.v3,
                    intersectionPoint
                )
            }

            val testIntersectionPoint = Vector3()
            Intersector.intersectRayTriangles(ray, vertices, indices, vertexSize, testIntersectionPoint)

            Gdx.app.log("vitalik", "Are intersection points equal: ${intersectionPoint == testIntersectionPoint}")

            Gdx.app.log("vitalik", "Intersection point: $intersectionPoint")
            Gdx.app.log("vitalik", "Test intersection point: $testIntersectionPoint")
            Gdx.app.log("vitalik", "Is intersection found: ${intersectionTriangle != null}")

            intersectionTriangle?.let { triangle ->
                val uv = getIntersectionUV(intersectionPoint, triangle, triangle.uvs)
                val textureX = (uv.x * instanceTextureAttribute!!.textureDescription.texture.width).toInt()
                val textureY = (uv.y * instanceTextureAttribute.textureDescription.texture.height).toInt()

                println("Texture coordinates: x: $textureX, y: $textureY")

                val texturePixmap = textureToPixmap(instanceTextureAttribute.textureDescription.texture)
                drawPointOnPixmap(texturePixmap, textureX, textureY, Color.RED)
                val newTexture = Texture(texturePixmap)
                instance!!.materials.first().set(TextureAttribute.createDiffuse(newTexture))
            }
        }


    }

//    private fun intersectRayTriangles(ray: Ray, vertices: FloatArray, indices: FloatArray, vertexSize: Int, intersectionPoint: Vector3, intersectionTriangle: ModelTriangle): Boolean {
//        val tmp1 = Vector3()
//        val tmp2 = Vector3()
//        val tmp3 = Vector3()
//
//        val intersectionPoint = Vector3()
//        val best = Vector3()
//
//        var minDist = Float.MAX_VALUE
//        var hit = false
//
//        if (indices.size % 3 != 0) throw RuntimeException("triangle list size is not a multiple of 3")
//
//        for (i in indices.indices step 3) {
//            val i1 = indices[i].toInt() * vertexSize
//            val i2 = indices[i + 1].toInt() * vertexSize
//            val i3 = indices[i + 2].toInt() * vertexSize
//
//            val isIntersectionFound = Intersector.intersectRayTriangle(
//                ray,
//                tmp1.set(vertices[i], vertices[i + 1], vertices[i + 2]),
//                tmp2.set(vertices[i + 3], vertices[i + 4], vertices[i + 5]),
//                tmp3.set(vertices[i + 6], vertices[i + 7], vertices[i + 8]),
//                intersectionPoint
//            )
//
//            if (isIntersectionFound) {
//                val dist = ray.origin.dst2(intersectionPoint)
//                if (dist < minDist) {
//                    minDist = dist
//                    best.set(intersectionPoint)
//                    hit = true
//                }
//            }
//        }
//
//        if (hit) {
//            intersectionPoint.set(best)
//            Gdx.app.log("vitalik", "Hit: $best")
//        }
//    }

    private fun textureToPixmap(texture: Texture): Pixmap {
        val textureData = texture.textureData
        if (!textureData.isPrepared) {
            textureData.prepare()
        }
        val pixmap = textureData.consumePixmap()
        val pixmapCopy = Pixmap(pixmap.width, pixmap.height, pixmap.format)
        pixmapCopy.drawPixmap(pixmap, 0, 0)
        textureData.disposePixmap()

        return pixmapCopy
    }

    private fun drawPointOnPixmap(pixmap: Pixmap, x: Int, y: Int, color: Color) {
//        pixmap.setColor(color)
//        pixmap.fillRectangle(x, y, 50, 50)
        pixmap.drawPixel(x, y, Color.rgba8888(color))
    }

    private fun getIntersectionUV(
        intersectionPoint: Vector3,
        triangle: ModelTriangle,
        uvs: Array<Vector2>
    ): Vector2 {
        val barycentricCoords = toBarycoord3D(
            intersectionPoint, triangle.v1, triangle.v2, triangle.v3
        )
//        val barycentricCoords = Vector2()
//        GeometryUtils.toBarycoord(
//            Vector2(intersectionPoint.x, intersectionPoint.y),
//            Vector2(triangle.v1.x, triangle.v1.y),
//            Vector2(triangle.v2.x, triangle.v2.y),
//            Vector2(triangle.v3.x, triangle.v3.y),
//            barycentricCoords
//        )

        val isInsideTheTriangle = GeometryUtils.barycoordInsideTriangle(Vector2(barycentricCoords.x, barycentricCoords.y))
        println("Is inside the triangle: $isInsideTheTriangle")

        val uv1 = uvs[0]
        val uv2 = uvs[1]
        val uv3 = uvs[2]

        return Vector2(
            barycentricCoords.z * uv1.x + barycentricCoords.x * uv2.x + barycentricCoords.y * uv3.x,
            barycentricCoords.z * uv1.y + barycentricCoords.x * uv2.y + barycentricCoords.y * uv3.y
        )
    }

    private fun toBarycoord3D(
        point: Vector3,
        a: Vector3,
        b: Vector3,
        c: Vector3,
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

    override fun dispose() {
        modelBatch?.dispose()
        backgroundSpriteBatch?.dispose()
        assets.dispose()
    }
}
