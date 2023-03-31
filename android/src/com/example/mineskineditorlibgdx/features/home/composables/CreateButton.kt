package com.example.mineskineditorlibgdx.features.home.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mineskineditorlibgdx.R

@Composable
fun CreateButton(
    @DrawableRes
    imageId: Int,
    @StringRes
    textId: Int,
    onClick: () -> Unit
) {
    val roundedCornerShape = remember { RoundedCornerShape(12.dp) }
    Column(
        modifier = Modifier
            .background(color = Color.White, shape = roundedCornerShape)
            .clickable(onClick = onClick)
            .clip(roundedCornerShape)
    ) {
        Text(
            text = stringResource(textId),
            color = Color.Black,
            style = MaterialTheme.typography.h2.copy(fontSize = 12.sp, lineHeight = 20.sp)
        )
        Image(painter = painterResource(imageId), contentDescription = null)
        Icon(
            painter = painterResource(R.drawable.ic_add_circle),
            tint = Color.Black,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun CreateButtonPreview() {
    CreateButton(imageId = R.drawable.ic_create_skin, textId = R.string.create_skin, onClick = { })
}