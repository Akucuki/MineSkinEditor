package com.example.mineskineditorlibgdx.features.libgdx.features.skinEditor3D

import androidx.annotation.FloatRange
import com.badlogic.gdx.graphics.Color
import com.example.mineskineditorlibgdx.features.libgdx.core.model.OnTextureColorPickListener
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintTool
import com.example.mineskineditorlibgdx.model.UiString


sealed class SkinEditor3DEvent {
    data class SetPaintTool(val paintTool: PaintTool) : SkinEditor3DEvent()
    data class SetToolSize(val toolSize: Int) : SkinEditor3DEvent()
    data class SetToolEffect(@FloatRange(0.0, 1.0) val toolEffect: Float) : SkinEditor3DEvent()
    data class SetOnTextureColorPickListener(val listener: OnTextureColorPickListener) : SkinEditor3DEvent()
    data class SetIsPaintEnabled(val isEnabled: Boolean) : SkinEditor3DEvent()
    data class SetPaintColor(val color: Color) : SkinEditor3DEvent()
    data class SetVisible(val isVisible: Boolean) : SkinEditor3DEvent()
    data class SaveSkinToLocalStorage(val name: UiString, val extension: String) : SkinEditor3DEvent()
}
