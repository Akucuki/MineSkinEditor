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
import com.example.mineskineditorlibgdx.model.ModelTriangle
import com.example.mineskineditorlibgdx.utils.*

class SkinEditorGame(
    private val debugLevel: DebugLevel = DebugLevel.LIGHT,
    private val debugColorPrimary: Color = Color.RED,
    private val debugColorSecondary: Color = Color.BLUE,
    private val modelFilename: String = "character_custom.g3db",
    private val modelTextureFilename: String = "texture_steve.png",
    private val backgroundTextureFilename: String = "bg_main.png"
) : Game() {

    private var environment: Environment? = null
    private var cam: Camera? = null
    private var camController: CameraInputController? = null
    private var backgroundSpriteBatch: SpriteBatch? = null
    private var modelBatch: ModelBatch? = null
    private var instance: ModelInstance? = null
    private var assets: AssetManager? = null
    private var backgroundTexture: Texture? = null
    private var areResourcesLoading: Boolean = true

    private val logTag = this::class.simpleName
    private var debugTextureSpriteBatch: SpriteBatch? = null
    private var debugShapeRenderer: ShapeRenderer? = null
    private val debugRayStartPoint = Vector3()
    private val debugRayEndPoint = Vector3()
    private var debugLastIntersectedTriangle: ModelTriangle? = null

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

    private var modelTriangles: List<ModelTriangle>? = null

    override fun create() {
        if (debugLevel == DebugLevel.FULL) Gdx.app.log(logTag, "create() called")

        assets = AssetManager()
        modelBatch = ModelBatch()
        debugTextureSpriteBatch = SpriteBatch()
        debugShapeRenderer = ShapeRenderer()
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

        assets?.load(modelFilename, Model::class.java)
        assets?.load(modelTextureFilename, Texture::class.java, parameter)
        assets?.load(backgroundTextureFilename, Texture::class.java)

        if (debugLevel == DebugLevel.FULL) Gdx.app.log(logTag, "create() finished")
    }

    private fun finishResourcesLoading() {
        if (debugLevel == DebugLevel.FULL) Gdx.app.log(logTag, "finishResourcesLoading() called")

        val characterModel = assets!!.get(modelFilename, Model::class.java)
        val modelTexture = assets!!.get(modelTextureFilename, Texture::class.java)
        backgroundTexture = assets!!.get(backgroundTextureFilename, Texture::class.java)

        instance = ModelInstance(characterModel)
        instance!!.setFirstMaterialTexture(modelTexture)
        modelTriangles = instance!!.triangles()

        areResourcesLoading = false

        if (debugLevel == DebugLevel.FULL) Gdx.app.log(logTag, "finishResourcesLoading() finished")
    }


    override fun render() {
        if (areResourcesLoading && assets?.update() == true) finishResourcesLoading()

        camController?.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        backgroundTexture?.let { texture ->
            backgroundSpriteBatch?.safeDraw(
                texture,
                0f,
                0f,
                Gdx.graphics.width.toFloat(),
                Gdx.graphics.height.toFloat()
            )
        }

        instance?.let { modelBatch?.safeRender(cam!!, it, environment!!) }

        if (debugLevel > DebugLevel.LIGHT) {
            modelTriangles?.let { triangles ->
                debugShapeRenderer?.safeDrawModelTriangles(cam!!, triangles, debugColorPrimary)
            }
            debugLastIntersectedTriangle?.let { lastTriangle ->
                debugShapeRenderer?.safeDrawModelTriangle(
                    cam!!,
                    lastTriangle,
                    debugColorSecondary
                )
            }
            debugShapeRenderer?.safeDrawLine(
                cam = cam!!,
                startVector = debugRayStartPoint,
                endVector = debugRayEndPoint,
                color = debugColorSecondary,
                lineWidth = 4f
            )
        }

        if (Gdx.input.justTouched()) {
            if (debugLevel > DebugLevel.NONE) Gdx.app.log(logTag, "Scene touched!")

            val ray = cam!!.getPickRay(
                Gdx.input.x.toFloat(),
                Gdx.input.y.toFloat(),
                0f,
                0f,
                Gdx.graphics.width.toFloat(),
                Gdx.graphics.height.toFloat()
            )

            if (debugLevel > DebugLevel.LIGHT) {
                debugRayStartPoint.set(ray.origin.sub(0f, .01f, 0f))
                debugRayEndPoint.set(ray.direction).scl(100f).add(debugRayStartPoint)
            }

            val intersectionTriangles = modelTriangles!!.filter {
                RaycastGeometry.intersectRayTriangle(ray, it, null)
            }
            val nearestIntersectedTriangle = intersectionTriangles.minByOrNull {
                cam!!.position.dst(it.centroid())
            }

            if (nearestIntersectedTriangle != null) {
                if (debugLevel > DebugLevel.LIGHT) {
                    debugLastIntersectedTriangle = nearestIntersectedTriangle
                }

                val intersectionPoint = Vector3()
                RaycastGeometry.intersectRayTriangle(
                    ray,
                    nearestIntersectedTriangle,
                    intersectionPoint
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

                if (debugLevel > DebugLevel.LIGHT) {
                    Gdx.app.log(
                        logTag,
                        "Intersection point: $intersectionPoint, UV: $uv, Texture: $textureX, $textureY"
                    )
                    Gdx.app.log(
                        logTag,
                        "UV: x: ${uv.x}, y: ${uv.y}"
                    )
                }
                if (debugLevel > DebugLevel.NONE) {
                    Gdx.app.log(
                        logTag,
                        "Texture: x: $textureX, y: $textureY"
                    )
                }

                if (!texture.textureData.isPrepared) texture.textureData.prepare()
                val pixmap = texture.textureData.consumePixmap()
                pixmap.drawPixel(textureX, textureY, Color.rgba8888(debugColorPrimary))
                val newTexture = Texture(pixmap)
                instance?.setFirstMaterialTexture(newTexture)
            }
        }
    }

    override fun dispose() {
        debugShapeRenderer?.dispose()
        debugTextureSpriteBatch?.dispose()
        modelBatch?.dispose()
        backgroundSpriteBatch?.dispose()
        assets?.dispose()
    }
}
