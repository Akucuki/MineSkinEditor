package com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap

object BrushTool : EditorTool {

    override fun use(parameters: EditorToolParameters) {
        require(parameters is EditorToolParameters.BrushParameters)
        with(parameters) {
            val centerX = x - thickness / 2
            val centerY = y - thickness / 2
            pixmap.apply {
                setColor(color)
                fillRectangle(centerX, centerY, thickness, thickness)
            }
        }
    }
}