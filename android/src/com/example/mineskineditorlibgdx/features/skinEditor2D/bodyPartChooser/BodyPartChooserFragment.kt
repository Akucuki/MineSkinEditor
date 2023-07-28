package com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.composables.TopAppBar
import com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.composables.SkinPartPreview
import com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.composables.SkinPreview
import dagger.hilt.android.AndroidEntryPoint

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


            MineSkinEditorTheme {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    contentDescription = null,
                    painter = painterResource(R.drawable.bg_main),
                    contentScale = ContentScale.FillBounds
                )
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
                    SkinPreview(modifier = Modifier.padding(top = 16.dp))
                    SkinPartPreview(modifier = Modifier.size(50.dp), partName = "TOP")
                }
            }
        }
    }
}