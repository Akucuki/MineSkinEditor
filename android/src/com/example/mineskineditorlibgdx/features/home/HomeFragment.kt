package com.example.mineskineditorlibgdx.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.application.theme.OrangeColor
import com.example.mineskineditorlibgdx.features.composables.TopAppBar
import com.example.mineskineditorlibgdx.features.home.composables.CreateButton

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
                    TopAppBar(
                        modifier = Modifier.align(Alignment.TopCenter),
                        trailingIconId = R.drawable.ic_search,
                        title = stringResource(R.string.crafty_craft),
                        onTrailingButtonClick = {}
                    )
                    Column(
                        modifier = Modifier.fillMaxSize().padding(top = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            CreateButton(
                                onClick = {},
                                imageId = R.drawable.img_create_skin,
                                textId = R.string.create_skin
                            )
                            CreateButton(
                                onClick = {},
                                imageId = R.drawable.img_create_addon,
                                textId = R.string.create_addon
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .background(
                                    color = OrangeColor,
                                    shape = remember { RoundedCornerShape(10.dp) }
                                )
                                .padding(horizontal = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                modifier = Modifier.width(170.dp).padding(top = 30.dp),
                                contentScale = ContentScale.FillWidth,
                                contentDescription = null,
                                painter = painterResource(R.drawable.img_crossbow)
                            )
                            Text(
                                modifier = Modifier.padding(vertical = 40.dp),
                                text = stringResource(R.string.home_caption),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.h2.copy(
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}