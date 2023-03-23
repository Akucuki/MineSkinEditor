package com.example.mineskineditorlibgdx.features.libgdx.core.model.brush

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

class Brush(private val thickness: Int) : PaintBrush {

    override fun paint(x: Int, y: Int, color: Color, pixmap: Pixmap) {
        val centerX = x - thickness / 2
        val centerY = y - thickness / 2
        pixmap.apply {
            setColor(color)
            fillRectangle(centerX, centerY, thickness, thickness)
        }
    }
}