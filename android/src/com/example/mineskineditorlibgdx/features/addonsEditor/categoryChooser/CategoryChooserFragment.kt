package com.example.mineskineditorlibgdx.features.addonsEditor.categoryChooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.composables.TopAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryChooserFragment : Fragment() {

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
                    TopAppBar(
                        trailingIconId = R.drawable.ic_search,
                        onTrailingButtonClick = { /*TODO*/ },
                        title = "Categories"
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(top = 48.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        CategoryButton(
                            modifier = Modifier.fillMaxWidth(),
                            label = "Create New Entities & Biomes",
                            onClick = {}
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CategoryButton(
                            modifier = Modifier.fillMaxWidth(),
                            label = "Create Your Own Sounds",
                            onClick = {}
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CategoryButton(
                            modifier = Modifier.fillMaxWidth(),
                            label = "Sounds & settings",
                            onClick = {}
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CategoryButton(
                            modifier = Modifier.fillMaxWidth(),
                            label = "Sounds & settings Sounds & settings Sounds & settings Sounds & settings",
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryButton(
    modifier: Modifier = Modifier,
    label: String,
    color: Color = Color.White,
    onClick: () -> Unit
) {
    val shape = remember { RoundedCornerShape(6.dp) }
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(color = color, shape = shape)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(60.dp),
            painter = painterResource(R.drawable.img_addons),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 18.dp),
            text = label,
            style = MaterialTheme.typography.body1.copy(
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Start
        )
        Icon(
            modifier = Modifier.size(26.dp),
            painter = painterResource(R.drawable.ic_chevron_right_stretched),
            contentDescription = null,
            tint = Color.Black
        )
    }
}