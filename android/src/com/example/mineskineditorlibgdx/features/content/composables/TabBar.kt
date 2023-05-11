package com.example.mineskineditorlibgdx.features.content.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.model.ContentTabType

@Composable
fun TabBar(
    modifier: Modifier = Modifier,
    selectedTabType: ContentTabType,
    onTabSelected: (ContentTabType) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ContentTabType.values().forEach { type ->
            TabButton(
                modifier = Modifier.weight(1f),
                type = type,
                isSelected = type == selectedTabType,
                onClick = { onTabSelected(type) }
            )
        }
    }
}

@Composable
private fun TabButton(
    modifier: Modifier = Modifier,
    type: ContentTabType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val roundedCornerShape = remember { RoundedCornerShape(6.dp) }
    Box(
        modifier = modifier
            .height(70.dp)
            .background(color = Color.White, shape = roundedCornerShape)
            .clip(roundedCornerShape)
            .clickable(onClick = onClick)
            .padding(6.dp),
    ) {
        Image(
            modifier = Modifier.size(32.dp).align(Alignment.TopCenter),
            painter = painterResource(type.imageId),
            contentDescription = null,
            contentScale = ContentScale.FillWidth
        )
        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = stringResource(type.textId),
            color = Color.Black,
            style = MaterialTheme.typography.h2.copy(fontSize = 12.sp, lineHeight = 20.sp)
        )
        if (isSelected) {
            Icon(
                modifier = Modifier.align(Alignment.BottomEnd),
                painter = painterResource(R.drawable.ic_check),
                tint = Color.Black,
                contentDescription = null
            )
        }
    }
}