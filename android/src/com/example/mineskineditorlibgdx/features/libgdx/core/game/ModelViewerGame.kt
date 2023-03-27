package com.example.mineskineditorlibgdx.features.libgdx.core.game

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
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.example.mineskineditorlibgdx.features.libgdx.core.model.OnPaintGestureEndListener
import com.example.mineskineditorlibgdx.features.libgdx.core.model.OnTextureColorPickListener
import com.example.mineskineditorlibgdx.features.libgdx.core.model.SkinEditor3D
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintTool
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PencilTool
import com.example.mineskineditorlibgdx.features.libgdx.core.utils.*
import com.example.mineskineditorlibgdx.model.ModelTriangle

class ModelViewerGame(
    private val debugLevel: DebugLevel = DebugLevel.LIGHT,
    private val debugColorPrimary: Color = Color.RED,
    private val debugColorSecondary: Color = Color.BLUE,
    private val modelFilename: String = "character_custom.g3db",
    private val modelTextureFilename: String = "texture_steve.png"
) : Game(), SkinEditor3D {

    private var environment: Environment? = null
    private var cam: Camera? = null
    private var camController: CameraInputController? = null
    private var backgroundSpriteBatch: SpriteBatch? = null
    private var modelBatch: ModelBatch? = null
    private var instance: ModelInstance? = null
    private var initialModelTexture: Texture? = null
    private var assets: AssetManager? = null
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

    private var isPaintEnabled = true
    private var paintTool: PaintTool = PencilTool
    private var paintColor: Color = Color.WHITE
    private var paintThickness: Int = 4
    private var noisePaintStrength: Float = 0.5f
    private var onPaintGestureEndListener: OnPaintGestureEndListener? = null
    private var onTextureColorPickListener: OnTextureColorPickListener? = null

    private var isVisible = true

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

//        camController = CameraInputController(cam)
        camController = object : CameraInputController(cam) {

            var isSwipeOnModel: Boolean? = null

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val ray = cam!!.getPickRay(screenX.toFloat(), screenY.toFloat())
                isSwipeOnModel = modelTriangles?.any {
                    RaycastGeometry.intersectRayTriangle(ray, it, null)
                }
                if (isSwipeOnModel == true) modelTouchProcessing(screenX, screenY)
                return super.touchDown(screenX, screenY, pointer, button)
            }

            override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
                if (isSwipeOnModel == true) {
                    modelTouchProcessing(screenX, screenY)
                } else {
                    super.touchDragged(screenX, screenY, pointer)
                }
                return false
            }

            private fun modelTouchProcessing(screenX: Int, screenY: Int) {
                val ray = cam!!.getPickRay(screenX.toFloat(), screenY.toFloat())

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
                    val texture = instance!!.firstMaterialTexture()
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
                    val pixmap = texture.textureData.safeConsumePixmap()
                    if (isPaintEnabled) {
                        paintTool.use(
                            textureX,
                            textureY,
                            paintColor,
                            pixmap,
                            paintThickness,
                            noisePaintStrength,
                            initialModelTexture!!.textureData.safeConsumePixmap()
                        )
                        val newTexture = Texture(pixmap)
                        instance?.setFirstMaterialTexture(newTexture)
                    }
                    onTextureColorPickListener?.invoke(Color(pixmap.getPixel(textureX, textureY)))
                }
            }
        }
        Gdx.input.inputProcessor = camController

        val parameter = TextureLoader.TextureParameter().apply {
            minFilter = Texture.TextureFilter.Nearest
            magFilter = Texture.TextureFilter.Nearest
            genMipMaps = false
        }

        assets?.load(modelFilename, Model::class.java)
        assets?.load(modelTextureFilename, Texture::class.java, parameter)

        if (debugLevel == DebugLevel.FULL) Gdx.app.log(logTag, "create() finished")
    }

    private fun finishResourcesLoading() {
        if (debugLevel == DebugLevel.FULL) Gdx.app.log(logTag, "finishResourcesLoading() called")

        val characterModel = assets!!.get(modelFilename, Model::class.java)
        initialModelTexture = assets!!.get(modelTextureFilename, Texture::class.java)

        instance = ModelInstance(characterModel)
        instance!!.setFirstMaterialTexture(initialModelTexture!!)
        modelTriangles = instance!!.triangles()

        areResourcesLoading = false

        if (debugLevel == DebugLevel.FULL) Gdx.app.log(logTag, "finishResourcesLoading() finished")
    }


    override fun render() {
        ScreenUtils.clear(0f, 0f, 0f, 0f)
        if (areResourcesLoading && assets?.update() == true) finishResourcesLoading()

        if (!isVisible) return

        camController?.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

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
    }

    override fun dispose() {
        debugShapeRenderer?.dispose()
        debugTextureSpriteBatch?.dispose()
        modelBatch?.dispose()
        assets?.dispose()
    }

    override fun setPaintColor(color: Color) {
        paintColor = color
    }

    override fun setPaintThickness(thickness: Int) {
        paintThickness = thickness
    }

    override fun setNoisePaintStrength(strength: Float) {
        noisePaintStrength = strength
    }

    override fun setTexture(texture: Texture) {
        instance?.setFirstMaterialTexture(texture)
    }

    override fun getTexture(): Texture = instance!!.firstMaterialTexture()

    override fun setPaintTool(paintTool: PaintTool) {
        this.paintTool = paintTool
    }

    override fun setOnPaintMotionEndListener(listener: OnPaintGestureEndListener) {
        onPaintGestureEndListener = listener
    }

    override fun setOnTextureColorPickListener(listener: OnTextureColorPickListener) {
        onTextureColorPickListener = listener
    }

    override fun setVisible(isVisible: Boolean) {
        this.isVisible = isVisible
    }

    override fun setIsPaintEnabled(isEnabled: Boolean) {
        this.isPaintEnabled = isEnabled
    }

    override fun saveSkinToAppStorage(name: String) {
        val currentTexture = instance!!.firstMaterialTexture()
        val currentPixmap = currentTexture.textureData.safeConsumePixmap()
        val file = Gdx.files.local(name)
        PixmapIO.writePNG(file, currentPixmap)
    }
}
