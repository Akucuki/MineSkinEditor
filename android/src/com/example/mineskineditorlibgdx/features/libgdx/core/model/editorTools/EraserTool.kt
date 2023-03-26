package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

object EraserTool : PaintTool {

    override fun use(
        x: Int,
        y: Int,
        color: Color,
        pixmap: Pixmap,
        thickness: Int,
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
                val initialPixmapColor = Color(initialPixmap.getPixel(i, j))
                pixmap.drawPixel(i, j, Color.rgba8888(initialPixmapColor))
            }
        }
    }
}