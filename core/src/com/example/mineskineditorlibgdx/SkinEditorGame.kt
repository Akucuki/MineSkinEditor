package com.example.mineskineditorlibgdx

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.example.mineskineditorlibgdx.utils.DebugLevel
import com.example.mineskineditorlibgdx.utils.RaycastGeometry
import com.example.mineskineditorlibgdx.utils.setFirstMaterialTexture

class SkinEditorGame(
    private val debugLevel: DebugLevel = DebugLevel.LIGHT
) : Game() {

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

    private val surroundingLights = arrayOf(
        // Left and right
        DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f),
        DirectionalLight().set(0.8f, 0.8f, 0.8f, 1f, -0.8f, 0.2f),
        // Facing and back
        DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -0.8f, -1f),
        DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, -0.8f, 1f),
        // Bottom
        DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, 1f, 0f)
    )

    override fun create() {
        assets = AssetManager()
        modelBatch = ModelBatch()
        textureDebugSpriteBatch = SpriteBatch()
        shapeDebugRenderer = ShapeRenderer()
        backgroundSpriteBatch = SpriteBatch()
        environment = Environment()
        environment?.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))

        surroundingLights.forEach { environment?.add(it) }

        cam = PerspectiveCamera(
            80f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        ).apply {
            position?.set(5f, 15f, 30f)
            lookAt(0f, 15f, 0f)
            near = 0.1f
            far = 100f
            update()
        }

        camController = CameraInputController(cam)
        Gdx.input.inputProcessor = camController

        val parameter = TextureLoader.TextureParameter().apply {
            minFilter = Texture.TextureFilter.Nearest
            magFilter = Texture.TextureFilter.Nearest
            genMipMaps = false
        }

        assets?.load("character_custom.g3db", Model::class.java)
        assets?.load("texture_steve.png", Texture::class.java, parameter)
        assets?.load("bg_main.png", Texture::class.java)
    }

    private fun finishResourcesLoading() {
        val characterModel = assets!!.get("character_custom.g3db", Model::class.java)
        val modelTexture = assets!!.get("texture_steve.png", Texture::class.java)
        backgroundTexture = assets!!.get("bg_main.png", Texture::class.java)

        instance = ModelInstance(characterModel)
        instance?.setFirstMaterialTexture(modelTexture)

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
            var meshesIteratedThroughCount = 0
            var intersectionsFoundCount = 0

            instance!!.model.meshes.forEach { mesh ->
                // DEBUG
                meshesIteratedThroughCount++

                val vertexSize = mesh.vertexSize / 4
                val vertices = FloatArray(mesh.numVertices * vertexSize)
                mesh.getVertices(vertices)
                val indices = ShortArray(mesh.numIndices)
                mesh.getIndices(indices)

                val triangles =
                    RaycastGeometry.getModelTriangles(indices, vertices, mesh)

                val intersectionTriangles = triangles.filter {
                    RaycastGeometry.intersectRayTriangle(
                        ray,
                        it.v1,
                        it.v2,
                        it.v3,
                        null
                    )
                }
                val nearestIntersectedTriangle = intersectionTriangles.minByOrNull {
                    cam!!.position.dst(RaycastGeometry.getTriangleCentroid(it))
                }

                if (nearestIntersectedTriangle != null) {
                    // DEBUG
                    intersectionsFoundCount++
                    Gdx.app.log(
                        "vitalik",
                        "Intersection found!"
                    )

                    val intersectionPoint = Vector3()
                    RaycastGeometry.intersectRayTriangle(
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
                    val uv = RaycastGeometry.getIntersectionUV(
                        intersectionPoint,
                        nearestIntersectedTriangle
                    )
                    val textureX = (uv.x * texture.width).toInt()
                    val textureY = (uv.y * texture.height).toInt()

                    // DEBUG
                    Gdx.app.log("vitalik", "Texture coords: x: $textureX, y: $textureY")

                    if (!texture.textureData.isPrepared) texture.textureData.prepare()
                    val pixmap = texture.textureData.consumePixmap()
                    pixmap.drawPixel(textureX, textureY, Color.rgba8888(debugColor))
                    val newTexture = Texture(pixmap)
                    instance?.setFirstMaterialTexture(newTexture)
                }
            }

            // DEBUG
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
