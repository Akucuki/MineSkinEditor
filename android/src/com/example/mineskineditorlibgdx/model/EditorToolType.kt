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

    fun isPaintTool(): Boolean = this != UNDO

    fun toAdditionalOptionsType(): AdditionalOptionsType {
        return when(this) {
            PENCIL -> AdditionalOptionsType.NONE
            NOISE -> AdditionalOptionsType.ALL
            BRUSH -> AdditionalOptionsType.SIZE
            FILL -> AdditionalOptionsType.NONE
            UNDO -> AdditionalOptionsType.NONE
            ERASER -> AdditionalOptionsType.SIZE
        }
    }
}

enum class EditorToolThickness(val thickness: Int) {
    PX1(1),
    PX2(2),
    PX4(4),
    PX6(6),
    PX8(8)
}

enum class AdditionalOptionsType {
    NONE,
    SIZE,
    ALL
}
