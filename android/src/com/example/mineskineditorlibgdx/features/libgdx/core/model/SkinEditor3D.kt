package com.example.mineskineditorlibgdx.features.libgdx.core.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintTool

typealias OnPaintGestureEndListener = () -> Unit

interface SkinEditor3D {

    fun setPaintColor(color: Color)
    fun setPaintThickness(thickness: Int)
    fun setNoisePaintStrength(strength: Float)
    fun setTexture(texture: Texture)
    fun getTexture(): Texture
    fun setPaintTool(paintTool: PaintTool)
    fun setOnPaintMotionEndListener(listener: OnPaintGestureEndListener)
}