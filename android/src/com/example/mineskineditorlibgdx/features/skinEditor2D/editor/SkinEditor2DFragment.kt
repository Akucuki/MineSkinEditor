package com.example.mineskineditorlibgdx.features.skinEditor2D.editor

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.skinEditor2D.editor.composables.SkinCanvas

class SkinEditor2DFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            val context = LocalContext.current
            val testBitmap = remember {
                BitmapFactory.decodeResource(context.resources, R.drawable.img_pixel_drawing_test)
            }

            MineSkinEditorTheme {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = null,
                    painter = painterResource(R.drawable.bg_main),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier.fillMaxSize().statusBarsPadding()
                ) {
                    SkinCanvas(
                        modifier = Modifier.size(400.dp),
                        bitmap = testBitmap!!,
                    )
                }
            }
        }
    }
}