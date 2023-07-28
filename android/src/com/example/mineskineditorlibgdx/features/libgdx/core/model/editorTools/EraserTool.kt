package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import androidx.annotation.FloatRange


object EraserTool : PaintTool {

    override fun use(
        x: Int,
        y: Int,
        color: androidx.compose.ui.graphics.Color,
        canvas: PaintCanvas,
        thickness: Int,
        @FloatRange(from = 0.0, to = 1.0)
        strength: Float,
        initialCanvas: PaintCanvas
    ) {
        val halfThickness = thickness / 2
        val startX = x - halfThickness
        val endX = x + halfThickness
        val startY = y - halfThickness
        val endY = y + halfThickness
        for (i in startX..endX) {
            for (j in startY..endY) {
                if (
                    i !in 0 until initialCanvas.width ||
                    j !in 0 until initialCanvas.height
                ) {
                    continue
                }
                val initialCanvasColor = initialCanvas.getPixel(i, j)
                canvas.drawPixel(i, j, initialCanvasColor)
            }
        }
    }
}