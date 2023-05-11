package com.example.mineskineditorlibgdx.features.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.mineskineditorlibgdx.features.content.composables.TabBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentFragment : Fragment() {

    private val viewModel: ContentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {

            val selectedTabType by viewModel.selectedTab.collectAsState()

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
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                                .padding(top = 48.dp)
                                .padding(horizontal = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            TabBar(
                                selectedTabType = selectedTabType,
                                onTabSelected = viewModel::onTabSelected
                            )
                        }
                    }
                }
            }
        }
    }
}