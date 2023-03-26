package com.example.mineskineditorlibgdx.features.libgdx.features

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.*
import com.example.mineskineditorlibgdx.model.EditorToolType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

private const val TOOL_TYPES = "toolTypes"
private const val ACTIVE_TOOL_TYPE = "activeToolType"
private const val ARE_OPTIONS_VISIBLE = "areOptionsVisible"

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
    val areOptionsVisible = handle.getStateFlow(
        ARE_OPTIONS_VISIBLE,
        false
    )



    fun onToolClick(toolType: EditorToolType) {
        when (toolType) {
            activeToolType.value -> {
                handle[ARE_OPTIONS_VISIBLE] = !areOptionsVisible.value
            }
            else -> {
                if (toolType != EditorToolType.UNDO) {
                    handle[ACTIVE_TOOL_TYPE] = toolType
                    val tool = when (toolType) {
                        EditorToolType.PENCIL -> PencilTool
                        EditorToolType.NOISE -> NoiseTool
                        EditorToolType.BRUSH -> BrushTool
                        EditorToolType.FILL -> FillTool
                        else -> PencilTool
                    }
                    events.trySend(SkinEditor3DEvent.SetPaintTool(tool))
                }
            }
        }
    }
}