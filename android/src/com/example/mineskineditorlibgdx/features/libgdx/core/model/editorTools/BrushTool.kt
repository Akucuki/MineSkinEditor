package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import androidx.annotation.FloatRange

object BrushTool : PaintTool {

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
        val centerX = x - thickness / 2 - 1
        val centerY = y - thickness / 2 - 1
        canvas.fillRectangle(centerX, centerY, thickness, thickness, color)
    }
}