package com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.composables.TopAppBar
import com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.composables.BodyPartChooserDialog
import com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.composables.SkinPartPreview
import com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.composables.SkinPreview
import com.example.mineskineditorlibgdx.model.BodyPartType
import com.example.mineskineditorlibgdx.utils.getBodyPartFace
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileNotFoundException

// TODO probably remove later, depending on how the previous screens will be implemented
const val SKIN_SOURCE_TEMPORAL = "skin-temp.png"

@AndroidEntryPoint
class BodyPartChooserFragment : Fragment() {

    private val viewModel: BodyPartChooserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {

            val testBitmap = remember {
                mutableStateOf(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.img_pixel_drawing_skin
                    )
                )
            }
            val selectedBodyPartType by viewModel.selectedBodyPartType.collectAsState()
            val isBodyPartChooserDialogVisible by viewModel.isBodyPartChooserDialogVisible.collectAsState()

//            LaunchedEffect(Unit) {
//                launch(Dispatchers.IO) {
//                    val fileOutputStream: FileOutputStream
//
//                    try {
//                        fileOutputStream = context.openFileOutput("skin-temp.png", Context.MODE_PRIVATE)
//                        testBitmap.value.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
//                        fileOutputStream.close()
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }

            LaunchedEffect(Unit) {
                launch(Dispatchers.IO) {
                    testBitmap.value = loadBitmap(context, SKIN_SOURCE_TEMPORAL)
                }
            }

            MineSkinEditorTheme {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = null,
                    painter = painterResource(R.drawable.bg_main),
                    contentScale = ContentScale.FillBounds
                )
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TopAppBar(
                            leadingIconId = R.drawable.ic_chevron_left,
                            onLeadingButtonClick = { /*TODO*/ },
                            trailingIconId = R.drawable.ic_search,
                            onTrailingButtonClick = { /*TODO*/ },
                            title = "Crafty Craft"
                        )
                        SkinPreview(
                            modifier = Modifier.padding(top = 16.dp),
                            headFace = testBitmap.value.getBodyPartFace(
                                BodyPartType.HEAD,
                                BodyPartType.FaceType.FRONT
                            ).asImageBitmap(),
                            bodyFace = testBitmap.value.getBodyPartFace(
                                BodyPartType.BODY,
                                BodyPartType.FaceType.FRONT
                            ).asImageBitmap(),
                            leftHandFace = testBitmap.value.getBodyPartFace(
                                BodyPartType.LEFT_ARM,
                                BodyPartType.FaceType.FRONT
                            ).asImageBitmap(),
                            rightHandFace = testBitmap.value.getBodyPartFace(
                                BodyPartType.RIGHT_ARM,
                                BodyPartType.FaceType.FRONT
                            ).asImageBitmap(),
                            leftLegFace = testBitmap.value.getBodyPartFace(
                                BodyPartType.LEFT_LEG,
                                BodyPartType.FaceType.FRONT
                            ).asImageBitmap(),
                            rightLegFace = testBitmap.value.getBodyPartFace(
                                BodyPartType.RIGHT_LEG,
                                BodyPartType.FaceType.FRONT
                            ).asImageBitmap(),
                            selectedBodyPartName = stringResource(selectedBodyPartType.partNameStringId),
                            onBodyPartClick = viewModel::onBodyPartFractionClick
                        )
                        val topRowFaces = listOf(BodyPartType.FaceType.TOP)
                        val middleFaces = listOf(
                            BodyPartType.FaceType.LEFT,
                            BodyPartType.FaceType.FRONT,
                            BodyPartType.FaceType.RIGHT,
                            BodyPartType.FaceType.BACK
                        )
                        val bottomRowFaces = listOf(BodyPartType.FaceType.BOTTOM)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            topRowFaces.forEach { faceType ->
                                SkinPartPreview(
                                    partName = stringResource(faceType.faceNameStringId),
                                    bitmap = testBitmap.value.getBodyPartFace(
                                        selectedBodyPartType,
                                        faceType
                                    )
                                        .asImageBitmap(),
                                    onClick = { viewModel.onSkinFaceClick(faceType) }
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            middleFaces.forEach { faceType ->
                                SkinPartPreview(
                                    partName = stringResource(faceType.faceNameStringId),
                                    bitmap = testBitmap.value.getBodyPartFace(
                                        selectedBodyPartType,
                                        faceType
                                    )
                                        .asImageBitmap(),
                                    onClick = { viewModel.onSkinFaceClick(faceType) }
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            bottomRowFaces.forEach { faceType ->
                                SkinPartPreview(
                                    partName = stringResource(faceType.faceNameStringId),
                                    bitmap = testBitmap.value.getBodyPartFace(
                                        selectedBodyPartType,
                                        faceType
                                    )
                                        .asImageBitmap(),
                                    onClick = { viewModel.onSkinFaceClick(faceType) }
                                )
                            }
                        }
                    }
                    if (isBodyPartChooserDialogVisible) {
                        BodyPartChooserDialog(
                            onDismiss = viewModel::onBodyPartChooserDialogDismiss,
                            onSelected = viewModel::onBodyPartChooserClick,
                        )
                    }
                }
            }
        }
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