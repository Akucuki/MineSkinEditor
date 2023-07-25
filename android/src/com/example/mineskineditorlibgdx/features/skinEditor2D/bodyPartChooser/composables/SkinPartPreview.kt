package com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun SkinPartPreview(
    modifier: Modifier = Modifier,
    partName: String
    // TODO pass bitmap here
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = partName,
            style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Medium),
            color = Color.White
        )
        Image(
            modifier = modifier.border(width = 2.dp, color = OrangeColor).padding(2.dp),
            painter = painterResource(R.drawable.img_addons),
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
    }
}