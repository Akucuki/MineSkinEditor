package com.example.mineskineditorlibgdx.features.libgdx.core.model.brush

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

class Pencil : PaintBrush {

    override fun paint(x: Int, y: Int, color: Color, pixmap: Pixmap) {
        pixmap.drawPixel(x, y, Color.rgba8888(color))
    }
}