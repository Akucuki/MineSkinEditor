package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color

interface PaintTool {

    fun use(
        x: Int,
        y: Int,
        color: Color,
        canvas: PaintCanvas,
        thickness: Int,
        @FloatRange(from = 0.0, to = 1.0)
        strength: Float,
        initialCanvas: PaintCanvas
    )
}

interface PaintCanvas {

    val width: Int
    val height: Int

    fun drawPixel(x: Int, y: Int, color: Color)

    fun fill(color: Color)

    fun fillRectangle(x: Int, y: Int, width: Int, height: Int, color: Color)

    fun getPixel(x: Int, y: Int): Color
}