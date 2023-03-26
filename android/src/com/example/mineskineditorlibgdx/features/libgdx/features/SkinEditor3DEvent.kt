package com.example.mineskineditorlibgdx.features.libgdx.features

import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintTool


sealed class SkinEditor3DEvent() {
    data class SetPaintTool(val paintTool: PaintTool) : SkinEditor3DEvent()
}
