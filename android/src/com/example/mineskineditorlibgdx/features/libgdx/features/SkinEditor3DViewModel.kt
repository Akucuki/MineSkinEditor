package com.example.mineskineditorlibgdx.features.libgdx.features

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.features.libgdx.core.model.editorTools.*
import com.example.mineskineditorlibgdx.features.libgdx.core.utils.toCompose
import com.example.mineskineditorlibgdx.model.*
import com.example.mineskineditorlibgdx.utils.NavigationDispatcher
import com.example.mineskineditorlibgdx.utils.toLibGDXColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val RECENT_COLORS_COUNT = 5
private const val SKIN_IMAGE_EXTENSION_SUFFIX = ".png"

private val initialColors = Array(RECENT_COLORS_COUNT) {
    ColorEntry(color = Color.White).toParcelableColorEntry()
}

private const val TOOL_TYPES = "toolTypes"
private const val ACTIVE_TOOL_TYPE = "activeToolType"
private const val ARE_TOOL_OPTIONS_VISIBLE = "areToolOptionsVisible"
private const val RECENT_COLORS = "recentColors"
private const val SELECTED_COLOR = "selectedColor"
private const val IS_IN_PIPETTE_MODE = "isInPipetteMode"
private const val SKIN_NAME = "skinName"
private const val SELECTED_EFFECT_VALUE = "selectedEffectValue"
private const val SELECTED_SIZE_TYPE = "selectedSizeType"

@HiltViewModel
class SkinEditor3DViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val navigationDispatcher: NavigationDispatcher
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
    val skinName: StateFlow<UiString> = handle.getStateFlow(
        SKIN_NAME,
        UiString.StringResource(R.string.my_new_skin)
    )
    private val _isSkinNameChooserVisible = MutableStateFlow(false)
    val isSkinNameChooserVisible = _isSkinNameChooserVisible.asStateFlow()
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

    init {
        events.trySend(SkinEditor3DEvent.SetPaintTool(PencilTool))
        events.trySend(
            SkinEditor3DEvent.SetOnTextureColorPickListener { onTextureColorPick(it.toCompose()) }
        )
        viewModelScope.launch {
            _isColorPickerDialogVisible.combine(_isSkinNameChooserVisible) { pickerVisible, nameChooserVisible ->
                pickerVisible || nameChooserVisible
            }.collect {
                events.trySend(SkinEditor3DEvent.SetVisible(!it))
            }
        }
    }


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
        handle[SELECTED_COLOR] = colorEntry.toParcelableColorEntry()
        events.trySend(SkinEditor3DEvent.SetPaintColor(colorEntry.color.toLibGDXColor()))
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

    fun onColorPickerClick() {
        _isColorPickerDialogVisible.value = true
    }

    fun onPipetteClick() {
        handle[IS_IN_PIPETTE_MODE] = !isInPipetteMode.value
        events.trySend(SkinEditor3DEvent.SetIsPaintEnabled(!isInPipetteMode.value))
    }

    private fun onTextureColorPick(color: Color) {
        if (isInPipetteMode.value) {
            onPipetteClick()
            onColorAdded(color)
        }
    }

    fun onColorPickerDialogCancelClick() {
        _isColorPickerDialogVisible.value = false
    }

    fun onColorPickerDialogOkClick(color: Color) {
        onColorAdded(color)
        _isColorPickerDialogVisible.value = false
    }

    fun onBackClick() {
        navigationDispatcher.emit { it.popBackStack() }
    }

    fun onSaveClick() {
        _isSkinNameChooserVisible.value = true
    }

    fun onSkinNameChooserCancelClick() {
        _isSkinNameChooserVisible.value = false
    }

    fun onSkinNameChooserSaveClick() {
        _isSkinNameChooserVisible.value = false
        events.trySend(
            SkinEditor3DEvent.SaveSkinToLocalStorage(
                skinName.value,
                SKIN_IMAGE_EXTENSION_SUFFIX
            )
        )
    }

    fun onSkinNameClearClick() {
        handle[SKIN_NAME] = UiString.SimpleString("")
    }

    fun onSkinNameChange(name: String) {
        handle[SKIN_NAME] = UiString.SimpleString(name)
    }

    fun onEffectChange(value: Float) {
        handle[SELECTED_EFFECT_VALUE] = value
    }

    fun onEffectChangeEnded() {
        events.trySend(SkinEditor3DEvent.SetToolEffect(effectValue.value))
    }

    fun onSizeChosen(type: EditorToolThickness) {
        handle[SELECTED_SIZE_TYPE] = type
        events.trySend(SkinEditor3DEvent.SetToolSize(type.thickness))
    }
}