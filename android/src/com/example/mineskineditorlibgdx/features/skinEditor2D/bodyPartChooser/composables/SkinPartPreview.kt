package com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun SkinPartPreview(
    modifier: Modifier = Modifier,
    partName: String,
    bitmap: ImageBitmap,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = partName,
            style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Medium),
            color = Color.White
        )
        Image(
            modifier = modifier
                .sizeIn(maxWidth = 100.dp, maxHeight = 100.dp)
                .aspectRatio(bitmap.width.toFloat() / bitmap.height.toFloat())
                .clickable(onClick = onClick)
                .border(width = 2.dp, color = OrangeColor)
                .padding(2.dp),
            bitmap = bitmap,
            contentScale = ContentScale.FillHeight,
            contentDescription = null,
            filterQuality = FilterQuality.None
        )
    }
}