package com.example.mineskineditorlibgdx.features.libgdx.features

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.*
import com.example.mineskineditorlibgdx.model.ColorEntry
import com.example.mineskineditorlibgdx.model.EditorToolType
import com.example.mineskineditorlibgdx.utils.toLibGDXColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val RECENT_COLORS_COUNT = 5

private val initialColors = Array(RECENT_COLORS_COUNT) { ColorEntry(color = Color.White) }

private const val TOOL_TYPES = "toolTypes"
private const val ACTIVE_TOOL_TYPE = "activeToolType"
private const val ARE_TOOL_OPTIONS_VISIBLE = "areToolOptionsVisible"
private const val RECENT_COLORS = "recentColors"
private const val SELECTED_COLOR = "selectedColor"

@HiltViewModel
class SkinEditor3DViewModel @Inject constructor(
    private val handle: SavedStateHandle,
) : ViewModel() {

    val events = Channel<SkinEditor3DEvent>(Channel.UNLIMITED)

    val toolTypes = handle.getStateFlow(
        TOOL_TYPES,
        EditorToolType.values()
    )
    val activeToolType = handle.getStateFlow(
        ACTIVE_TOOL_TYPE,
        EditorToolType.PENCIL
    )
    val areToolOptionsVisible = handle.getStateFlow(
        ARE_TOOL_OPTIONS_VISIBLE,
        false
    )
    var recentColors = handle.getStateFlow(
        RECENT_COLORS,
        LinkedList<ColorEntry>().apply {
            addAll(initialColors)
        }
    )
    val selectedColor = handle.getStateFlow(
        SELECTED_COLOR,
        initialColors.first()
    )
    private val _isColorPickerDialogVisible = MutableStateFlow(false)
    val isColorPickerDialogVisible = _isColorPickerDialogVisible.asStateFlow()

    init {
        events.trySend(SkinEditor3DEvent.SetPaintTool(PencilTool))
        viewModelScope.launch {
            _isColorPickerDialogVisible.collect { isPickerVisible ->
                events.trySend(SkinEditor3DEvent.SetVisible(!isPickerVisible))
            }
        }
    }


    fun onToolClick(toolType: EditorToolType) {
        handle[ARE_TOOL_OPTIONS_VISIBLE] = false
        when (toolType) {
            activeToolType.value -> {
                handle[ARE_TOOL_OPTIONS_VISIBLE] =
                    toolType.isPaintTool() && !areToolOptionsVisible.value
            }
            else -> {
                if (toolType.isPaintTool()) {
                    handle[ACTIVE_TOOL_TYPE] = toolType
                    val tool = when (toolType) {
                        EditorToolType.PENCIL -> PencilTool
                        EditorToolType.NOISE -> NoiseTool
                        EditorToolType.BRUSH -> BrushTool
                        EditorToolType.FILL -> FillTool
                        else -> EraserTool
                    }
                    events.trySend(SkinEditor3DEvent.SetPaintTool(tool))
                }
            }
        }
    }

    fun onColorClick(colorEntry: ColorEntry) {
        handle[SELECTED_COLOR] = colorEntry
        events.trySend(SkinEditor3DEvent.SetPaintColor(colorEntry.color.toLibGDXColor()))
    }

    private fun onColorAdded(color: Color) {
        val newColorEntry = ColorEntry(color = color)
        val colors = recentColors.value
//        val colors = recentColors
        colors.removeLast()
        colors.addFirst(newColorEntry)
//        recentColors.value = colors
        handle[RECENT_COLORS] = colors
        onColorClick(newColorEntry)
    }

    fun onColorPickerClick() {
        _isColorPickerDialogVisible.value = true
    }

    fun onPipetteClick() {

    }

    fun onColorPickerDialogCancelClick() {
        _isColorPickerDialogVisible.value = false
    }

    fun onColorPickerDialogOkClick(color: Color) {
        onColorAdded(color)
        _isColorPickerDialogVisible.value = false
    }
}