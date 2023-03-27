package com.example.mineskineditorlibgdx.features.libgdx.core.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintTool

typealias OnPaintGestureEndListener = () -> Unit
typealias OnTextureColorPickListener = (color: Color) -> Unit

interface SkinEditor3D {

    fun setVisible(isVisible: Boolean)
    fun setPaintColor(color: Color)
    fun setPaintThickness(thickness: Int)
    fun setNoisePaintStrength(strength: Float)
    fun setTexture(texture: Texture)
    fun getTexture(): Texture
    fun setPaintTool(paintTool: PaintTool)
    fun setOnPaintMotionEndListener(listener: OnPaintGestureEndListener)
    fun setOnTextureColorPickListener(listener: OnTextureColorPickListener)
    fun setIsPaintEnabled(isEnabled: Boolean)
}