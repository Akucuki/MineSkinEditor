package com.example.mineskineditorlibgdx.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.composables.TopAppBar

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {


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
                            title = stringResource(R.string.skin_editor),
                            onTrailingButtonClick = {}
                        )
                        Row() {

                        }
                    }
                }
            }
        }
    }
}