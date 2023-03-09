package com.example.mineskineditorlibgdx

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.BaseJsonReader
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.ScreenUtils

class SkinEditorGame : ApplicationAdapter() {

    private var modelBatch: ModelBatch? = null
    private lateinit var environment: Environment
    private lateinit var cam: Camera
    private var instance: ModelInstance? = null
    private lateinit var camController: CameraInputController
    private lateinit var assets: AssetManager
    private var loading: Boolean = false
    private var spriteBatch: SpriteBatch? = null
    private var backgroundTexture: Texture? = null

    override fun create() {
        spriteBatch = SpriteBatch()
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

        cam = PerspectiveCamera(60f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam.position.set(0f, 1f, -4f)
        cam.lookAt(0f, 1f, 0f)
        cam.near = 1f
        cam.far = 10f
        cam.update()

        camController = CameraInputController(cam)
        Gdx.input.inputProcessor = camController

        assets = AssetManager()
        assets.load("humanoid.g3db", Model::class.java)
        loading = true
    }

    private fun doneLoading() {
        val skinModel = assets.get("humanoid.g3db", Model::class.java)
        instance = ModelInstance(skinModel)
        instance!!.materials.forEach {
            val textureAttribute = it.get(TextureAttribute.Diffuse) as TextureAttribute
            val texture = textureAttribute.textureDescription.texture
            texture.setFilter(
                Texture.TextureFilter.Nearest,
                Texture.TextureFilter.Nearest
            )
        }
        loading = false
    }

    override fun render() {
        if (loading && assets.update()) doneLoading()
        camController.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        spriteBatch?.begin()
        spriteBatch?.draw(
            backgroundTexture,
            0f,
            0f,
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        )
        spriteBatch?.end()

        modelBatch?.begin(cam)
        instance?.let { modelBatch?.render(it, environment) }
        modelBatch?.end()
    }

    override fun dispose() {
        modelBatch?.dispose()
        assets.dispose()
    }
}