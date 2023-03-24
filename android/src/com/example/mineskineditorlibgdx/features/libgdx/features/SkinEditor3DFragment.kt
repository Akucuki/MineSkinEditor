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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.libgdx.core.game.ModelViewerGame
import com.example.mineskineditorlibgdx.features.libgdx.features.composables.BottomBar
import com.example.mineskineditorlibgdx.model.EditorToolType
import com.example.mineskineditorlibgdx.model.EditorToolWrapper

class SkinEditor3DFragment : AndroidFragmentApplication() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {

            val libGDXAppConfig = remember {
                AndroidApplicationConfiguration().apply { a = 8 }
            }
            val libGDXView = remember {
                initializeForView(
                    ModelViewerGame(),
                    libGDXAppConfig
                ).apply {
                    if (this is SurfaceView) {
                        setZOrderOnTop(true)
                        this.holder.setFormat(PixelFormat.TRANSLUCENT)
                    }
                }
            }
            val editorToolWrappers = remember {
                EditorToolType.values().mapIndexed { index, item ->
                    EditorToolWrapper(
                        type = item,
                        isActive = index == 0
                    )
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
                        BottomBar(tools = editorToolWrappers, onToolClick = {})
                    }
                }
            }
        }
    }
}