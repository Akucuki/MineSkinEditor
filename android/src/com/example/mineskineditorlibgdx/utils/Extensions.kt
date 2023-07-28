package com.example.mineskineditorlibgdx.utils

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintCanvas

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

inline fun <T> tryOrNull(f: () -> T) =
    try {
        f()
    } catch (_: Exception) {
        null
    }