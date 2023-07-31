package com.example.mineskineditorlibgdx.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.BaseCanvas
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintCanvas
import com.example.mineskineditorlibgdx.model.BodyPart
import com.example.mineskineditorlibgdx.model.BodyPartFace
import com.example.mineskineditorlibgdx.model.BodyPartType
import com.example.mineskineditorlibgdx.model.FaceCoordinates

fun Color.toLibGDXColor(): com.badlogic.gdx.graphics.Color {
    return com.badlogic.gdx.graphics.Color(red, green, blue, alpha)
}

fun Bitmap.asBaseCanvas(): BaseCanvas {
    val bitmap = this
    return object : BaseCanvas {
        override val width: Int
            get() = bitmap.width
        override val height: Int
            get() = bitmap.height

        override fun getPixel(x: Int, y: Int): Color {
            return Color(bitmap.getPixel(x, y))
        }
    }
}

fun Bitmap.asPaintCanvas(): PaintCanvas {
    require(this.isMutable) {
        IllegalStateException("Bitmap must be mutable for drawing")
    }
    val bitmap = this
    return object : PaintCanvas {
        override val width: Int
            get() = bitmap.width
        override val height: Int
            get() = bitmap.height

        override fun drawPixel(x: Int, y: Int, color: Color) {
            Log.d("vitalik", "Color inside drawPixel: $color")
            bitmap.setPixel(x, y, color.toArgb())
        }

        override fun fill(color: Color) {
            bitmap.eraseColor(color.toArgb())
        }

        override fun fillRectangle(
            x: Int,
            y: Int,
            width: Int,
            height: Int,
            color: Color
        ) {
            for (bitmapX in x until x + width) {
                for (bitmapY in y until y + height) {
                    if (
                        bitmapX in 0 until bitmap.width &&
                        bitmapY in 0 until bitmap.height
                    ) {
                        bitmap.setPixel(bitmapX, bitmapY, color.toArgb())
                    }
                }
            }
        }

        override fun getPixel(x: Int, y: Int): Color {
            return Color(bitmap.getPixel(x, y))
        }
    }
}

private fun Bitmap.copyPartFace(faceCoordinates: FaceCoordinates): Bitmap {
    Log.d("vitalik", "faceCoordinates: $faceCoordinates")
    Log.d("vitalik", "this.size: ${this.width}x${this.height}")
    return Bitmap.createBitmap(
        this,
        faceCoordinates.x,
        faceCoordinates.y,
        faceCoordinates.width,
        faceCoordinates.height,
    )
}

fun Bitmap.getBodyPartFace(bodyPartType: BodyPartType, faceType: BodyPartType.FaceType): Bitmap {
    return this.copyPartFace(getBodyPartFaceCoordinates(bodyPartType, faceType))
}

fun Bitmap.composeBitmapWithFace(
    bodyPartType: BodyPartType,
    faceType: BodyPartType.FaceType,
    updatedFace: Bitmap
): Bitmap {
    val newBitmap = Bitmap.createBitmap(this.width, this.height, this.config)
    val canvas = Canvas(newBitmap)

    // Draw the original skin onto the new Bitmap.
    // This is necessary because the new Bitmap is initially empty.
    canvas.drawBitmap(this, 0f, 0f, null)

    // Get the coordinates of the face.
    val faceCoordinates = getBodyPartFaceCoordinates(bodyPartType, faceType)

    // Draw the updated face onto the new Bitmap.
    canvas.drawBitmap(updatedFace, faceCoordinates.x.toFloat(), faceCoordinates.y.toFloat(), null)

    return newBitmap
}

fun Bitmap.getBodyPartFaces(bodyPartType: BodyPartType): List<BodyPartFace> {
    Log.d("vitalik", "body part bitmap: ${this.width}x${this.height}")
    return BodyPartType.FaceType.values().map { faceType ->
        BodyPartFace(faceType, this.getBodyPartFace(bodyPartType, faceType))
    }
}

fun Bitmap.getBodyPart(bodyPartType: BodyPartType): BodyPart {
    return BodyPart(
        bodyPartType,
        this.getBodyPartFaces(bodyPartType)
    )
}

inline fun <T> tryOrNull(f: () -> T) =
    try {
        f()
    } catch (_: Exception) {
        null
    }