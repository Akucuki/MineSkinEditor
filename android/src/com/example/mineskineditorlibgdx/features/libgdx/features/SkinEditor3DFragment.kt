package com.example.mineskineditorlibgdx.features.libgdx.features

import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.composables.TopAppBar
import com.example.mineskineditorlibgdx.features.libgdx.core.game.ModelViewerGame
import com.example.mineskineditorlibgdx.features.libgdx.core.model.SkinEditor3D
import com.example.mineskineditorlibgdx.features.libgdx.features.composables.BottomBar
import com.example.mineskineditorlibgdx.features.libgdx.features.composables.ColorPickerDialog
import com.example.mineskineditorlibgdx.features.libgdx.features.composables.RecentColorsBar
import com.example.mineskineditorlibgdx.features.libgdx.features.composables.SkinNameBar
import com.example.mineskineditorlibgdx.features.libgdx.features.composables.SkinNameChooserDialog
import com.example.mineskineditorlibgdx.model.UiString
import com.example.mineskineditorlibgdx.model.toColorEntries
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class SkinEditor3DFragment : AndroidFragmentApplication() {

    private val viewModel: SkinEditor3DViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {

            val events = remember(viewModel.events, viewLifecycleOwner) {
                viewModel.events.receiveAsFlow().flowWithLifecycle(
                    viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
                )
            }

            val skinEditor3D: SkinEditor3D = remember { ModelViewerGame() }
            val libGDXAppConfig = remember {
                AndroidApplicationConfiguration().apply { a = 8 }
            }
            val libGDXView = remember(skinEditor3D) {
                initializeForView(
                    skinEditor3D as ModelViewerGame, libGDXAppConfig
                ).apply {
                    if (this is SurfaceView) {
                        setZOrderOnTop(true)
                        setBackgroundColor(Color.TRANSPARENT)
                        this.holder.setFormat(PixelFormat.TRANSLUCENT)
                    }
                }
            }
            val context = LocalContext.current
            val editorToolTypes by viewModel.toolTypes.collectAsState()
            val activeEditorToolType by viewModel.activeToolType.collectAsState()
            val recentColors by viewModel.recentColors.collectAsState()
            val selectedColor by viewModel.selectedColor.collectAsState()
            val isColorPickerDialogVisible by viewModel.isColorPickerDialogVisible.collectAsState()
            val isInPipetteMode by viewModel.isInPipetteMode.collectAsState()
            val skinNameUiString: UiString by viewModel.skinName.collectAsState()
            val isSkinNameChooserDialogVisible by viewModel.isSkinNameChooserVisible.collectAsState()
            val additionalOptionsType by viewModel.additionalOptionsType.collectAsState()
            val areAdditionalOptionsVisible by viewModel.areToolOptionsVisible.collectAsState()
            val effectValue by viewModel.effectValue.collectAsState()
            val selectedSize by viewModel.selectedThicknessType.collectAsState()

            LaunchedEffect(Unit) {
                events.collect { event ->
                    when (event) {
                        is SkinEditor3DEvent.SetPaintTool -> {
                            val paintTool = event.paintTool
                            skinEditor3D.setPaintTool(paintTool)
                        }
                        is SkinEditor3DEvent.SetPaintColor -> {
                            val color = event.color
                            skinEditor3D.setPaintColor(color)
                        }
                        is SkinEditor3DEvent.SetVisible -> {
                            val isVisible = event.isVisible
                            skinEditor3D.setVisible(isVisible)
                        }
                        is SkinEditor3DEvent.SetOnTextureColorPickListener -> {
                            val listener = event.listener
                            skinEditor3D.setOnTextureColorPickListener(listener)
                        }
                        is SkinEditor3DEvent.SetIsPaintEnabled -> {
                            val isEnabled = event.isEnabled
                            skinEditor3D.setIsPaintEnabled(isEnabled)
                        }
                        is SkinEditor3DEvent.SaveSkinToLocalStorage -> {
                            val skinName = event.name
                            val extension = event.extension
                            skinEditor3D.saveSkinToAppStorage(skinName.asString(context) + extension)
                        }
                        is SkinEditor3DEvent.SetToolEffect -> {
                            val effect = event.toolEffect
                            skinEditor3D.setNoisePaintStrength(effect)
                        }
                        is SkinEditor3DEvent.SetToolSize -> {
                            val size = event.toolSize
                            skinEditor3D.setPaintThickness(size)
                        }
                    }
                }
            }

            MineSkinEditorTheme {
                Box {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = null,
                        painter = painterResource(R.drawable.bg_main),
                        contentScale = ContentScale.FillBounds
                    )
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TopAppBar(
                            leadingIconId = R.drawable.ic_chevron_left,
                            trailingIconId = R.drawable.ic_save,
                            onLeadingButtonClick = viewModel::onBackClick,
                            onTrailingButtonClick = viewModel::onSaveClick,
                            title = stringResource(R.string.skin_editor)
                        )
                        SkinNameBar(
                            name = skinNameUiString.asString(),
                            onNameChange = viewModel::onSkinNameChange
                        )
                        AndroidView(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), factory = {
                            libGDXView
                        })
                        RecentColorsBar(
                            colorEntries = recentColors.toColorEntries(),
                            onColorClick = viewModel::onColorClick,
                            selectedColor = selectedColor.toColorEntry(),
                            onColorPickerClick = viewModel::onColorPickerClick,
                            onPipetteClick = viewModel::onPipetteClick,
                            isPipetteActive = isInPipetteMode,
                            additionalOptionsType = additionalOptionsType,
                            onSizeChosen = viewModel::onSizeChosen,
                            onEffectChange = viewModel::onEffectChange,
                            onEffectChangeEnded = viewModel::onEffectChangeEnded,
                            areAdditionalOptionsVisible = areAdditionalOptionsVisible,
                            effectValue = effectValue,
                            selectedSize = selectedSize
                        )
                        BottomBar(
                            toolTypes = editorToolTypes,
                            selectedToolType = activeEditorToolType,
                            onToolClick = viewModel::onToolClick
                        )
                    }
                    if (isColorPickerDialogVisible) {
                        ColorPickerDialog(
                            onDismissClick = viewModel::onColorPickerDialogCancelClick,
                            onOkClick = viewModel::onColorPickerDialogOkClick,
                            initialColor = selectedColor.toColorEntry().color
                        )
                    }
                    if (isSkinNameChooserDialogVisible) {
                        SkinNameChooserDialog(
                            onDismissClick = viewModel::onSkinNameChooserCancelClick,
                            onSaveClick = viewModel::onSkinNameChooserSaveClick,
                            skinName = skinNameUiString.asString(),
                            onSkinNameChange = viewModel::onSkinNameChange,
                            onClearNameClick = viewModel::onSkinNameClearClick
                        )
                    }
                }
            }
        }
    }
}