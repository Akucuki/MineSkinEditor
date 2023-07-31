package com.example.mineskineditorlibgdx.features.skinEditor2D.editor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.SKIN_SOURCE_TEMPORAL
import com.example.mineskineditorlibgdx.features.skinEditor2D.editor.composables.SkinCanvas
import com.example.mineskineditorlibgdx.model.BodyPartType
import com.example.mineskineditorlibgdx.model.ColorEntry
import com.example.mineskineditorlibgdx.model.EditorToolType
import com.example.mineskineditorlibgdx.model.toColorEntries
import com.example.mineskineditorlibgdx.utils.asBaseCanvas
import com.example.mineskineditorlibgdx.utils.composeBitmapWithFace
import com.example.mineskineditorlibgdx.utils.getBodyPartFace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.FileOutputStream

@AndroidEntryPoint
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

            // TODO refactor!
            val selectedBodyPartType = viewModel.selectedBodyPartType.collectAsState()
            val selectedBodyPartFace = viewModel.selectedFaceType.collectAsState()

            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()

            val skinFullBitmap = remember { mutableStateOf<Bitmap?>(null) }
            val skinPartInitialFaceBitmap = remember(skinFullBitmap.value) {
                mutableStateOf(
                    skinFullBitmap.value?.getBodyPartFace(
                        selectedBodyPartType.value,
                        selectedBodyPartFace.value
                    )
                )
            }
            val skinPartFaceBitmap = remember(skinFullBitmap.value) {
                mutableStateOf(
                    skinFullBitmap.value?.getBodyPartFace(
                        selectedBodyPartType.value,
                        selectedBodyPartFace.value
                    )
                )
            }

            LaunchedEffect(selectedBodyPartType.value, selectedBodyPartFace.value) {
                Log.d(
                    "vitalik",
                    "Trying to load full bitmap. " +
                            "selectedBodyPartType.value = ${selectedBodyPartType.value}," +
                            " selectedBodyPartFace.value = ${selectedBodyPartFace.value}"
                )
                launch(Dispatchers.IO) {
                    val bitmap = loadBitmap(context, SKIN_SOURCE_TEMPORAL)
                    skinFullBitmap.value = bitmap
                }
            }

            //TODO remove
            LaunchedEffect(selectedBodyPartType.value) {
                Log.d("vitalik", "selectedBodyPartType.value = ${selectedBodyPartType.value}")
            }

            BackHandler(
                onBack = {
                    // TODO save the image before exiting
                    coroutineScope.launch(Dispatchers.IO) {
                        saveEditedSkin(
                            context,
                            fullBitmap = skinFullBitmap.value!!,
                            bodyPartType = selectedBodyPartType.value,
                            faceBitmap = skinPartFaceBitmap.value!!,
                            bodyPartFaceType = selectedBodyPartFace.value
                        )
                    }
                    viewModel.onBackClick()
                }
            )

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
                        bitmap = skinPartFaceBitmap,
                        gridStrokeColor = Color.White,
                        paintTool = selectedToolImpl,
                        paintColor = selectedColor,
                        paintToolStrength = effectValue,
                        paintToolThicknessType = selectedThicknessType,
                        initialBaseCanvas = skinPartInitialFaceBitmap.value?.asBaseCanvas(),
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ) {
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

private suspend fun saveEditedSkin(
    context: Context,
    fullBitmap: Bitmap,
    faceBitmap: Bitmap,
    bodyPartType: BodyPartType,
    bodyPartFaceType: BodyPartType.FaceType
) = withContext(Dispatchers.IO) {
    val fullBitmapMutable = fullBitmap.copy(fullBitmap.config, true)
    val faceBitmapMutable = faceBitmap.copy(faceBitmap.config, true)
    val updatedBitmap = fullBitmapMutable.composeBitmapWithFace(
        bodyPartType = bodyPartType,
        faceType = bodyPartFaceType,
        updatedFace = faceBitmapMutable
    )
    val fileOutputStream: FileOutputStream
    try {
        fileOutputStream = context.openFileOutput(SKIN_SOURCE_TEMPORAL, Context.MODE_PRIVATE)
        updatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun loadBitmap(context: Context, filename: String): Bitmap? {
    return try {
        val fileInputStream = context.openFileInput(filename)
        BitmapFactory.decodeStream(fileInputStream).copy(Bitmap.Config.ARGB_8888, true)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}
