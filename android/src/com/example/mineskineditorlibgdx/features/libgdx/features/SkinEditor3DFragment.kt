package com.example.mineskineditorlibgdx.features.libgdx.features

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.libgdx.core.game.ModelViewerGame
import com.example.mineskineditorlibgdx.features.libgdx.features.composables.BottomBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class SkinEditor3DFragment : AndroidFragmentApplication() {

    private val viewModel: SkinEditor3DViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {

            val events = remember(viewModel.events, viewLifecycleOwner) {
                viewModel.events.receiveAsFlow().flowWithLifecycle(
                    viewLifecycleOwner.lifecycle,
                    Lifecycle.State.STARTED
                )
            }

            val modelViewerGame = remember { ModelViewerGame() }
            val libGDXAppConfig = remember {
                AndroidApplicationConfiguration().apply { a = 8 }
            }
            val libGDXView = remember(modelViewerGame) {
                initializeForView(
                    modelViewerGame,
                    libGDXAppConfig
                ).apply {
                    if (this is SurfaceView) {
                        setZOrderOnTop(true)
                        this.holder.setFormat(PixelFormat.TRANSLUCENT)
                    }
                }
            }
            val editorToolTypes by viewModel.toolTypes.collectAsState()
            val activeEditorToolType by viewModel.activeToolType.collectAsState()

            LaunchedEffect(Unit) {
                events.collect { event ->
                    when (event) {
                        is SkinEditor3DEvent.SetPaintTool -> {
                            val paintBrush = event.editorPaintBrush
                            modelViewerGame.paintTool = paintBrush
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
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { libGDXView }
                    )
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        BottomBar(
                            toolTypes = editorToolTypes,
                            selectedToolType = activeEditorToolType,
                            onToolClick = viewModel::onToolClick
                        )
                    }
                }
            }
        }
    }
}