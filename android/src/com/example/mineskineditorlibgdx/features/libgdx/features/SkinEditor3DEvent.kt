package com.example.mineskineditorlibgdx.features.libgdx.features

import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintBrush

sealed class SkinEditor3DEvent() {
    data class SetPaintTool(val editorPaintBrush: PaintBrush) : SkinEditor3DEvent()
}
