package com.example.mineskineditorlibgdx.features.libgdx.features

import com.badlogic.gdx.graphics.Color
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintTool


sealed class SkinEditor3DEvent() {
    data class SetPaintTool(val paintTool: PaintTool) : SkinEditor3DEvent()
    data class SetPaintColor(val color: Color) : SkinEditor3DEvent()
    data class SetVisible(val isVisible: Boolean) : SkinEditor3DEvent()
}
