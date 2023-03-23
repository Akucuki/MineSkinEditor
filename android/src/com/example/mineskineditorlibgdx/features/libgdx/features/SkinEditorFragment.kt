package com.example.mineskineditorlibgdx.features.libgdx.features

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.viewinterop.AndroidView
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.example.mineskineditorlibgdx.features.libgdx.core.game.ModelViewerGame

class SkinEditorFragment : AndroidFragmentApplication() {

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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Cyan)
            ) {
                Text(text = "Hello World!")
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { libGDXView }
                )
            }
        }
    }
}