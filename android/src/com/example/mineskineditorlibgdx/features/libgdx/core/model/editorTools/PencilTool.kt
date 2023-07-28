package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

object PencilTool : PaintTool {

    override fun use(
        x: Int,
        y: Int,
        color: androidx.compose.ui.graphics.Color,
        canvas: PaintCanvas,
        thickness: Int,
        strength: Float,
        initialCanvas: PaintCanvas
    ) {
        canvas.drawPixel(x, y, color)
    }
}