package com.example.mineskineditorlibgdx.utils

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintCanvas
import com.example.mineskineditorlibgdx.model.BodyPart
import com.example.mineskineditorlibgdx.model.BodyPartFace
import com.example.mineskineditorlibgdx.model.BodyPartType

fun Color.toLibGDXColor(): com.badlogic.gdx.graphics.Color {
    return com.badlogic.gdx.graphics.Color(red, green, blue, alpha)
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

data class FaceCoordinates(val x: Int, val y: Int, val width: Int, val height: Int)

fun Bitmap.getBodyPart(bodyPartType: BodyPartType): BodyPart {
    val top = when (bodyPartType) {
        BodyPartType.HEAD -> FaceCoordinates(8, 0, 8, 8)
        BodyPartType.BODY -> FaceCoordinates(20, 16, 8, 4)
        BodyPartType.RIGHT_ARM -> FaceCoordinates(44, 16, 4, 4)
        BodyPartType.LEFT_ARM -> FaceCoordinates(36, 48, 4, 4)
        BodyPartType.RIGHT_LEG -> FaceCoordinates( 4, 16, 4, 4)
        BodyPartType.LEFT_LEG -> FaceCoordinates(20, 48, 4, 4)
    }
    val right = when (bodyPartType) {
        BodyPartType.HEAD -> FaceCoordinates(0, 8, 8, 8)
        BodyPartType.BODY -> FaceCoordinates(16, 20, 4, 12)
        BodyPartType.RIGHT_ARM -> FaceCoordinates(40, 20, 4, 12)
        BodyPartType.LEFT_ARM -> FaceCoordinates(40, 52, 4, 12)
        BodyPartType.RIGHT_LEG -> FaceCoordinates( 0, 20, 4, 12)
        BodyPartType.LEFT_LEG -> FaceCoordinates(24, 52, 4, 12)
    }
    val front = when (bodyPartType) {
        BodyPartType.HEAD -> FaceCoordinates(8, 8, 8, 8)
        BodyPartType.BODY -> FaceCoordinates(20, 20, 8, 12)
        BodyPartType.RIGHT_ARM -> FaceCoordinates(44, 20, 4, 12)
        BodyPartType.LEFT_ARM -> FaceCoordinates(36, 52, 4, 12)
        BodyPartType.RIGHT_LEG -> FaceCoordinates( 4, 20, 4, 12)
        BodyPartType.LEFT_LEG -> FaceCoordinates(20, 52, 4, 12)
    }
    val left = when (bodyPartType) {
        BodyPartType.HEAD -> FaceCoordinates(8, 8, 8, 8)
        BodyPartType.BODY -> FaceCoordinates(20, 20, 8, 12)
        BodyPartType.RIGHT_ARM -> FaceCoordinates(44, 20, 4, 12)
        BodyPartType.LEFT_ARM -> FaceCoordinates(36, 52, 4, 12)
        BodyPartType.RIGHT_LEG -> FaceCoordinates( 4, 20, 4, 12)
        BodyPartType.LEFT_LEG -> FaceCoordinates(20, 52, 4, 12)
    }
    return BodyPart(
        type = bodyPartType,
        top = Bitmap.createBitmap(this, 8, 0, 8, 8),
        right = Bitmap.createBitmap(this, 0, 8, 8, 12),
        front = Bitmap.createBitmap(this, 8, 8, 8, 12),
        left = Bitmap.createBitmap(this, 16, 8, 8, 12),
        back = Bitmap.createBitmap(this, 24, 8, 8, 12),
        bottom = Bitmap.createBitmap(this, 16, 0, 8, 8)
    )
}

inline fun <T> tryOrNull(f: () -> T) =
    try {
        f()
    } catch (_: Exception) {
        null
    }