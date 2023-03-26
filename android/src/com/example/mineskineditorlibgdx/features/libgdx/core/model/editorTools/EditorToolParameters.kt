package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import androidx.annotation.FloatRange
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

sealed class EditorToolParameters {
    abstract class PaintParameters(
        open val x: Int,
        open val y: Int,
        open val color: Color,
        open val pixmap: Pixmap
    ) : EditorToolParameters()

    data class PencilParameters(
        override val x: Int,
        override val y: Int,
        override val color: Color,
        override val pixmap: Pixmap
    ) : PaintParameters(x, y, color, pixmap)
    data class NoiseParameters(
        override val x: Int,
        override val y: Int,
        override val color: Color,
        override val pixmap: Pixmap,
        val thickness: Int,
        @FloatRange(from = 0.0, to = 1.0)
        val strength: Float
    ): PaintParameters(x, y, color, pixmap)
    data class BrushParameters(
        override val x: Int,
        override val y: Int,
        override val color: Color,
        override val pixmap: Pixmap,
        val thickness: Int
    ) : PaintParameters(x, y, color, pixmap)
    data class FillParameters(
        override val x: Int,
        override val y: Int,
        override val color: Color,
        override val pixmap: Pixmap
    ) : PaintParameters(x, y, color, pixmap)
}