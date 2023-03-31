package com.example.mineskineditorlibgdx.features.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    @DrawableRes
    leadingIconId: Int? = null,
    @DrawableRes
    trailingIconId: Int,
    onLeadingButtonClick: () -> Unit = {},
    onTrailingButtonClick: () -> Unit,
    title: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(48.dp)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        leadingIconId?.let {
            IconButton(
                onClick = onLeadingButtonClick,
            ) {
                Icon(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.h2,
            color = Color.White
        )
        IconButton(
            onClick = onTrailingButtonClick
        ) {
            Icon(
                painter = painterResource(id = trailingIconId),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}