package com.example.mineskineditorlibgdx.utils

import androidx.compose.ui.graphics.Color

fun Color.toLibGDXColor(): com.badlogic.gdx.graphics.Color {
    return com.badlogic.gdx.graphics.Color(red, green, blue, alpha)
}

inline fun <T> tryOrNull(f: () -> T) =
    try {
        f()
    } catch (_: Exception) {
        null
    }