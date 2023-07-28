package com.example.mineskineditorlibgdx.features.libgdx.core.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.TextureData
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.RenderableProvider
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintCanvas
import com.example.mineskineditorlibgdx.model.ModelTriangle
import com.example.mineskineditorlibgdx.utils.toLibGDXColor

const val BYTES_IN_FLOAT = 4

fun Color.toCompose(): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color(r, g, b, a)
}

fun TextureData.safeConsumePixmap(): Pixmap {
    if (!isPrepared) prepare()
    return consumePixmap()
}

fun Pixmap.asPaintCanvas(): PaintCanvas {
    val pixmap = this
    return object : PaintCanvas {

        override val width: Int
            get() = pixmap.width
        override val height: Int
            get() = pixmap.height


        override fun drawPixel(x: Int, y: Int, color: androidx.compose.ui.graphics.Color) {
            pixmap.drawPixel(x, y, Color.rgba8888(color.toLibGDXColor()))
        }

        override fun fill(color: androidx.compose.ui.graphics.Color) {
            pixmap.apply {
                setColor(color.toLibGDXColor())
                fill()
            }
        }

        override fun fillRectangle(
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            color: androidx.compose.ui.graphics.Color
        ) {
            pixmap.apply {
                setColor(color.toLibGDXColor())
                fillRectangle(x, y, width, height)
            }
        }

        override fun getPixel(x: Int, y: Int): androidx.compose.ui.graphics.Color {
            return Color(pixmap.getPixel(x, y)).toCompose()
        }
    }
}

fun ShapeRenderer.safeDrawLine(
    cam: Camera,
    startVector: Vector3,
    endVector: Vector3,
    color: Color,
    lineWidth: Float = 2f
) {
    Gdx.gl.glLineWidth(lineWidth)
    projectionMatrix = cam.combined
    begin(ShapeRenderer.ShapeType.Line)
    this.color = color
    line(startVector, endVector)
    end()
}

fun ShapeRenderer.safeDrawModelTriangles(
    cam: Camera,
    triangles: List<ModelTriangle>,
    color: Color,
    lineWidth: Float = 2f
) {
    triangles.forEach { triangle -> safeDrawModelTriangle(cam, triangle, color, lineWidth) }
}

fun ShapeRenderer.safeDrawModelTriangle(
    cam: Camera,
    triangle: ModelTriangle,
    color: Color,
    lineWidth: Float = 2f
) {
    Gdx.gl.glLineWidth(lineWidth)
    projectionMatrix = cam.combined
    begin(ShapeRenderer.ShapeType.Line)
    this.color = color
    line(triangle.v1, triangle.v2)
    line(triangle.v2, triangle.v3)
    line(triangle.v3, triangle.v1)
    end()
}

fun ModelBatch.safeRender(
    cam: Camera,
    renderableProvider: RenderableProvider,
    environment: Environment
) {
    begin(cam)
    render(renderableProvider, environment)
    end()
}

fun SpriteBatch.safeDraw(
    texture: Texture,
    x: Float,
    y: Float,
    width: Float,
    height: Float
) {
    begin()
    draw(texture, x, y, width, height)
    end()
}

fun ModelInstance.setFirstMaterialTexture(texture: Texture) {
    materials.first().set(TextureAttribute.createDiffuse(texture))
}

fun ModelInstance.firstMaterialTexture(): Texture {
    val instanceTextureAttribute =
        materials.first().get(TextureAttribute.Diffuse) as TextureAttribute
    return instanceTextureAttribute.textureDescription.texture
}

fun ModelInstance.triangles(): List<ModelTriangle> {
    val triangles = mutableListOf<ModelTriangle>()
    model.meshes.forEach { mesh ->
        val vertexSize = mesh.vertexSize / BYTES_IN_FLOAT
        val vertices = FloatArray(mesh.numVertices * vertexSize)
        mesh.getVertices(vertices)
        val indices = ShortArray(mesh.numIndices)
        mesh.getIndices(indices)

        for (i in indices.indices step 3) {
            val stride = mesh.vertexSize / BYTES_IN_FLOAT
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
                vertexAttributes.findByUsage(VertexAttributes.Usage.TextureCoordinates).offset / BYTES_IN_FLOAT

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
    }
    return triangles
}