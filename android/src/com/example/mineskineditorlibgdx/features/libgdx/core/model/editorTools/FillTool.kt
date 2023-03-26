package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

object FillTool : EditorTool {

    override fun use(parameters: EditorToolParameters) {
        require(parameters is EditorToolParameters.FillParameters)
        with(parameters) {
            pixmap.apply {
                setColor(color)
                fill()
            }
        }
    }
}