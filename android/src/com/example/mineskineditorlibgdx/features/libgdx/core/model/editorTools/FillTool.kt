package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import androidx.annotation.FloatRange

object FillTool : PaintTool {

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
        canvas.fill(color)
    }
}