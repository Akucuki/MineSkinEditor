package com.example.mineskineditorlibgdx.features.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.composables.TopAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {

            MineSkinEditorTheme {
                MineSkinEditorTheme {
                    Box {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = null,
                            painter = painterResource(R.drawable.bg_main),
                            contentScale = ContentScale.FillBounds
                        )
                        TopAppBar(
                            modifier = Modifier.align(Alignment.TopCenter),
                            trailingIconId = R.drawable.ic_search,
                            title = stringResource(R.string.crafty_craft),
                            onTrailingButtonClick = {}
                        )
                        Column(
                            modifier = Modifier.fillMaxSize().padding(top = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                        }
                    }
                }
            }
        }
    }
}