package com.example.mineskineditorlibgdx.features.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(48.dp)
            .padding(horizontal = 10.dp),
    ) {
        leadingIconId?.let {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
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
            modifier = Modifier.align(Alignment.Center),
            text = title,
            style = MaterialTheme.typography.h2,
            color = Color.White
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
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