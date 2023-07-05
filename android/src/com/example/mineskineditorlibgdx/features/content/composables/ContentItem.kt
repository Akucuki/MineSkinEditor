package com.example.mineskineditorlibgdx.features.content.composables

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun ContentItem(
    modifier: Modifier = Modifier,
    title: String,
    imageUri: Uri,
    @DrawableRes
    typeImageId: Int
) {
    val painter = rememberAsyncImagePainter(model = imageUri)

    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = OrangeColor,
                    shape = remember { RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp) }
                )
                .padding(vertical = 6.dp),
            text = title,
            style = MaterialTheme.typography.body1,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Inside
            )
            Image(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = typeImageId),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}