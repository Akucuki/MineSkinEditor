package com.example.mineskineditorlibgdx.features.skinEditor2D.editor

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.BrushTool
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.EraserTool
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.FillTool
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.NoiseTool
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PaintTool
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.PencilTool
import com.example.mineskineditorlibgdx.model.AdditionalOptionsType
import com.example.mineskineditorlibgdx.model.ColorEntry
import com.example.mineskineditorlibgdx.model.EditorToolThickness
import com.example.mineskineditorlibgdx.model.EditorToolType
import com.example.mineskineditorlibgdx.model.ParcelableColorEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.LinkedList
import javax.inject.Inject

private const val RECENT_COLORS_COUNT = 5
private const val SKIN_IMAGE_EXTENSION_SUFFIX = ".png"

val initialColors = Array(RECENT_COLORS_COUNT) {
    ColorEntry(color = Color.White).toParcelableColorEntry()
}

private const val SELECTED_TOOL_TYPE = "selectedToolType"
private const val ARE_TOOL_OPTIONS_VISIBLE = "areToolOptionsVisible"
private const val RECENT_COLORS = "recentColors"
private const val SELECTED_COLOR = "selectedColor"
private const val IS_IN_PIPETTE_MODE = "isInPipetteMode"
private const val SKIN_NAME = "skinName"
private const val SELECTED_EFFECT_VALUE = "selectedEffectValue"
private const val SELECTED_SIZE_TYPE = "selectedSizeType"

//private const val SELECTED_TOOL_IMPL = "selectedToolImpl"

@HiltViewModel
class SkinEditor2DViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    val activeToolType = handle.getStateFlow(SELECTED_TOOL_TYPE, EditorToolType.PENCIL)
    val areToolOptionsVisible = handle.getStateFlow(ARE_TOOL_OPTIONS_VISIBLE, false)
    var recentColors = handle.getStateFlow(
        RECENT_COLORS,
        LinkedList<ParcelableColorEntry>().apply {
            addAll(initialColors)
        }
    )
    val selectedColor = handle.getStateFlow(
        SELECTED_COLOR,
        initialColors.first()
    )
    private val _isColorPickerDialogVisible = MutableStateFlow(false)
    val isColorPickerDialogVisible = _isColorPickerDialogVisible.asStateFlow()
    val isInPipetteMode = handle.getStateFlow(
        IS_IN_PIPETTE_MODE,
        false
    )
    val additionalOptionsType = activeToolType.map {
        it.toAdditionalOptionsType()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AdditionalOptionsType.NONE
    )
    val effectValue = handle.getStateFlow(
        SELECTED_EFFECT_VALUE,
        0f
    )
    val selectedThicknessType = handle.getStateFlow(
        SELECTED_SIZE_TYPE,
        EditorToolThickness.PX1
    )

    val selectedToolImpl = MutableStateFlow<PaintTool>(PencilTool)

    fun onToolClick(toolType: EditorToolType) {
        when (toolType) {
            activeToolType.value -> {
                if (
                    toolType.isPaintTool() &&
                    toolType.toAdditionalOptionsType() != AdditionalOptionsType.NONE
                ) {
                    handle[ARE_TOOL_OPTIONS_VISIBLE] = !areToolOptionsVisible.value
                }
            }
            else -> {
                handle[ARE_TOOL_OPTIONS_VISIBLE] = false
                if (toolType.isPaintTool()) {
                    handle[SELECTED_TOOL_TYPE] = toolType
                    val tool = when (toolType) {
                        EditorToolType.PENCIL -> PencilTool
                        EditorToolType.NOISE -> NoiseTool
                        EditorToolType.BRUSH -> BrushTool
                        EditorToolType.FILL -> FillTool
                        else -> EraserTool
                    }
                    selectedToolImpl.value = tool
                }
            }
        }
    }

    fun onColorClick(color: ColorEntry) {
        handle[SELECTED_COLOR] = color.toParcelableColorEntry()
    }

    fun onColorPickerClick() {
        _isColorPickerDialogVisible.value = true
    }

    fun onPipetteClick() {
        handle[IS_IN_PIPETTE_MODE] = true
    }

    fun onSizeChosen(size: EditorToolThickness) {
        handle[SELECTED_SIZE_TYPE] = size
    }

    fun onEffectChange(value: Float) {
        handle[SELECTED_EFFECT_VALUE] = value
    }

    fun onColorPickerDismissClick() {
        _isColorPickerDialogVisible.value = false
    }

    private fun onColorAdded(color: Color) {
        val newColorEntry = ColorEntry(color = color)
        val colors = recentColors.value
//        val colors = recentColors
        colors.removeLast()
        colors.addFirst(newColorEntry.toParcelableColorEntry())
//        recentColors.value = colors
        handle[RECENT_COLORS] = colors
        onColorClick(newColorEntry)
    }

    fun onColorPickerOkClick(color: ColorEntry) {
        onColorAdded(color.color)
        _isColorPickerDialogVisible.value = false
    }

}