package com.example.mineskineditorlibgdx.features.libgdx.core.model.brush

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

private val paintThickness = arrayOf(1, 2, 3, 4, 5)

interface PaintBrush {

    fun paint(x: Int, y: Int, color: Color, pixmap: Pixmap)
}