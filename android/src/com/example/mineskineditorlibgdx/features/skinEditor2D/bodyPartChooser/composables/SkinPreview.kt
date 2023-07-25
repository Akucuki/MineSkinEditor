package com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.application.theme.GrayColor
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun SkinPreview(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = GrayColor)
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        BodyPartFraction(modifier = Modifier.width(144.dp))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(
                brush = remember {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f),
                            Color.Transparent,
                        )
                    )
                }
            )
    )
}

@Composable
private fun BodyPartFraction(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Body",
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.W400),
            color = OrangeColor
        )
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.White)
        Text(
            text = "Part of skin",
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.W400),
            color = Color.White
        )
    }
}