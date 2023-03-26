package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import androidx.annotation.FloatRange
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

object BrushTool : PaintTool {

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
        val centerX = x - thickness / 2
        val centerY = y - thickness / 2
        pixmap.apply {
            setColor(color)
            fillRectangle(centerX, centerY, thickness, thickness)
        }
    }
}