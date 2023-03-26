package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

object PencilTool : EditorTool {

    override fun use(parameters: EditorToolParameters) {
        require(parameters is EditorToolParameters.PencilParameters)
        with(parameters) {
            pixmap.drawPixel(x, y, Color.rgba8888(color))
        }
    }
}