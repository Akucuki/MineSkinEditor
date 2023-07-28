package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object NoiseTool : PaintTool {

    override fun use(
        x: Int,
        y: Int,
        color: Color,
        canvas: PaintCanvas,
        thickness: Int,
        @FloatRange(from = 0.0, to = 1.0)
        strength: Float,
        initialCanvas: PaintCanvas
    ) {
        val halfThickness = thickness / 2
        val startX = x - halfThickness
        val endX = x + halfThickness - 1
        val startY = y - halfThickness
        val endY = y + halfThickness - 1
        for (i in startX..endX) {
            for (j in startY..endY) {
                if (
                    i !in 0 until initialCanvas.width ||
                    j !in 0 until initialCanvas.height
                ) {
                    continue
                }
                val noiseColor = getNoiseForColor(color, strength)
                canvas.drawPixel(i, j, noiseColor)
            }
        }
    }

    private fun getNoiseForColor(color: Color, strength: Float): Color {
        val noise = Random.nextFloat() * strength
        val r = (color.red + noise).coerceIn(0f, 1f)
        val g = (color.green + noise).coerceIn(0f, 1f)
        val b = (color.blue + noise).coerceIn(0f, 1f)
        return Color(red = r, green = g, blue = b, alpha = color.alpha)
    }
}