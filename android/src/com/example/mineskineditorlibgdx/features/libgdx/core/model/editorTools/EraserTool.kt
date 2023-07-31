package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import androidx.annotation.FloatRange


object EraserTool : PaintTool {

    override fun use(
        x: Int,
        y: Int,
        color: androidx.compose.ui.graphics.Color,
        canvas: PaintCanvas,
        initialBaseCanvas: BaseCanvas,
        thickness: Int,
        @FloatRange(from = 0.0, to = 1.0)
        strength: Float,
    ) {
        val halfThickness = thickness / 2
        val startX = x - halfThickness
        val endX = x + halfThickness
        val startY = y - halfThickness
        val endY = y + halfThickness
        for (i in startX..endX) {
            for (j in startY..endY) {
                if (
                    i !in 0 until initialBaseCanvas.width ||
                    j !in 0 until initialBaseCanvas.height
                ) {
                    continue
                }
                val initialCanvasColor = initialBaseCanvas.getPixel(i, j)
                canvas.drawPixel(i, j, initialCanvasColor)
            }
        }
    }
}