package com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser

import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.model.BodyPartType
import com.example.mineskineditorlibgdx.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val SELECTED_BODY_PART_TYPE = "selected_body_part_type"
private const val IS_BODY_PART_CHOOSER_DIALOG_VISIBLE = "is_body_part_chooser_dialog_visible"

const val SELECTED_BODY_PART_FACE = "selected_body_part_face"

@HiltViewModel
class BodyPartChooserViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val navDispatcher: NavigationDispatcher
) : ViewModel() {

    val selectedBodyPartType = handle.getStateFlow(SELECTED_BODY_PART_TYPE, BodyPartType.HEAD)
    val isBodyPartChooserDialogVisible =
        handle.getStateFlow(IS_BODY_PART_CHOOSER_DIALOG_VISIBLE, false)

    fun onSkinFaceClick(faceType: BodyPartType.FaceType) {
        Log.d(
            "vitalik",
            "Skin face chosen. Face type: $faceType, selectedBodyPartType: ${selectedBodyPartType.value}"
        )
        navDispatcher.emit {
            it.navigate(
                R.id.goFragmentSkinEditor2D,
                bundleOf(
                    SELECTED_BODY_PART_TYPE to selectedBodyPartType.value,
                    SELECTED_BODY_PART_FACE to faceType
                )
            )
        }
    }

    fun onBodyPartFractionClick() {
        handle[IS_BODY_PART_CHOOSER_DIALOG_VISIBLE] = true
    }

    fun onBodyPartChooserClick(bodyPartType: BodyPartType) {
        handle[SELECTED_BODY_PART_TYPE] = bodyPartType
        handle[IS_BODY_PART_CHOOSER_DIALOG_VISIBLE] = false
    }

    fun onBodyPartChooserDialogDismiss() {
        handle[IS_BODY_PART_CHOOSER_DIALOG_VISIBLE] = false
    }
}