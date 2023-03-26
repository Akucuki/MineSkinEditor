package com.example.mineskineditorlibgdx.model

import com.example.mineskineditorlibgdx.R

enum class EditorToolType {
    PENCIL,
    NOISE,
    BRUSH,
    FILL,
    UNDO,
    ERASER;

    fun nameStringId(): Int {
        return when(this) {
            PENCIL -> R.string.pencil
            NOISE -> R.string.noise
            BRUSH -> R.string.brush
            FILL -> R.string.fill
            UNDO -> R.string.undo
            ERASER -> R.string.eraser
        }
    }

    fun iconId(): Int {
        return when(this) {
            PENCIL -> R.drawable.ic_pencil
            NOISE -> R.drawable.ic_noise
            BRUSH -> R.drawable.ic_brush
            FILL -> R.drawable.ic_fill
            UNDO -> R.drawable.ic_undo
            ERASER -> R.drawable.ic_eraser
        }
    }
}
