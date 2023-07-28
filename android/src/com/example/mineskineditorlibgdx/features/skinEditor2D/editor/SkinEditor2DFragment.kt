package com.example.mineskineditorlibgdx.features.skinEditor2D.editor

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.composables.skinEditor.BottomBar
import com.example.mineskineditorlibgdx.features.composables.skinEditor.ColorPickerDialog
import com.example.mineskineditorlibgdx.features.composables.skinEditor.RecentColorsBar
import com.example.mineskineditorlibgdx.features.skinEditor2D.editor.composables.SkinCanvas
import com.example.mineskineditorlibgdx.model.ColorEntry
import com.example.mineskineditorlibgdx.model.EditorToolType
import com.example.mineskineditorlibgdx.model.toColorEntries
import com.example.mineskineditorlibgdx.utils.asPaintCanvas

class SkinEditor2DFragment : Fragment() {

    private val viewModel: SkinEditor2DViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            val selectedToolType by viewModel.activeToolType.collectAsState()
            val areToolOptionsVisible by viewModel.areToolOptionsVisible.collectAsState()
            val recentColors by viewModel.recentColors.collectAsState()
            val selectedColor = viewModel.selectedColor.collectAsState()
            val isColorPickerDialogVisible by viewModel.isColorPickerDialogVisible.collectAsState()
            val isInPipetteMode by viewModel.isInPipetteMode.collectAsState()
            val additionalOptionsType by viewModel.additionalOptionsType.collectAsState()
            val effectValue = viewModel.effectValue.collectAsState()
            val selectedThicknessType = viewModel.selectedThicknessType.collectAsState()

            val selectedToolImpl = viewModel.selectedToolImpl.collectAsState()

            val context = LocalContext.current
            val testBitmap = remember {
                BitmapFactory.decodeResource(context.resources, R.drawable.img_pixel_drawing_test)
            }
            val testBitmapMutable = remember {
                testBitmap.copy(testBitmap.config, true)
            }

            MineSkinEditorTheme {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = null,
                    painter = painterResource(R.drawable.bg_main),
                    contentScale = ContentScale.FillBounds
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    SkinCanvas(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = testBitmap!!,
                        gridStrokeColor = Color.White,

                        paintTool = selectedToolImpl,
                        paintColor = selectedColor,
                        initialPaintCanvas = testBitmapMutable.asPaintCanvas(),
                        paintToolStrength = effectValue,
                        paintToolThicknessType = selectedThicknessType
                    )
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)) {
                        RecentColorsBar(
                            colorEntries = recentColors.toColorEntries(),
                            onColorClick = viewModel::onColorClick,
                            selectedColor = selectedColor.value.toColorEntry(),
                            onColorPickerClick = viewModel::onColorPickerClick,
                            onPipetteClick = viewModel::onPipetteClick,
                            isPipetteActive = isInPipetteMode,
                            additionalOptionsType = additionalOptionsType,
                            onSizeChosen = viewModel::onSizeChosen,
                            onEffectChange = viewModel::onEffectChange,
                            onEffectChangeEnded = {},
                            areAdditionalOptionsVisible = areToolOptionsVisible,
                            effectValue = effectValue.value,
                            selectedSize = selectedThicknessType.value
                        )
                        BottomBar(
                            modifier = Modifier.fillMaxWidth(),
                            toolTypes = EditorToolType.values(),
                            selectedToolType = selectedToolType,
                            onToolClick = viewModel::onToolClick
                        )
                    }
                    if (isColorPickerDialogVisible) {
                        ColorPickerDialog(
                            onDismissClick = viewModel::onColorPickerDismissClick,
                            onOkClick = { viewModel.onColorPickerOkClick(ColorEntry(color = it)) },
                            initialColor = selectedColor.value.toColorEntry().color
                        )
                    }
                }
            }
        }
    }
}