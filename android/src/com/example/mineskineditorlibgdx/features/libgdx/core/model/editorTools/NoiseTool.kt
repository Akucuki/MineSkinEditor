package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import androidx.annotation.FloatRange
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import kotlin.random.Random

object NoiseTool : PaintTool {

    override fun use(
        x: Int,
        y: Int,
        color: Color,
        pixmap: Pixmap,
        thickness: Int,
        @FloatRange(from = 0.0, to = 1.0)
        strength: Float,
        initialPixmap: Pixmap
    ) {
        val halfThickness = thickness / 2
        val startX = x - halfThickness
        val endX = x + halfThickness
        val startY = y - halfThickness
        val endY = y + halfThickness
        for (i in startX..endX) {
            for (j in startY..endY) {
                val noiseColor = getNoiseForColor(color, strength)
                pixmap.drawPixel(i, j, Color.rgba8888(noiseColor))
            }
        }
    }

    private fun getNoiseForColor(color: Color, strength: Float): Color {
        val noise = Random.nextFloat() * strength
        val r = (color.r + noise).coerceIn(0f, 1f)
        val g = (color.g + noise).coerceIn(0f, 1f)
        val b = (color.b + noise).coerceIn(0f, 1f)
        return Color(r, g, b, color.a)
    }
}