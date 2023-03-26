package com.example.mineskineditorlibgdx.features.libgdx.features.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.PurpleColor

@Composable
fun RecentColorsBar(
    modifier: Modifier = Modifier,
    colors: List<Color>,
    selectedColor: Color,
    onColorClick: (Color) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = Color.White)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(R.drawable.ic_color_picker),
                contentDescription = null,
                tint = PurpleColor
            )
        }
        colors.forEach { color ->
            ColorButton(
                color = color,
                onClick = { onColorClick(color) },
                isSelected = color == selectedColor
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(R.drawable.ic_pipette),
                contentDescription = null,
                tint = Color.Black,
            )
        }
    }
}

@Composable
private fun ColorButton(
    modifier: Modifier = Modifier,
    color: Color,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    val roundedCornerShape = remember { RoundedCornerShape(4.dp) }
    val borderModifier = if (isSelected) {
        Modifier.border(width = 2.dp, color = Color.Black, shape = roundedCornerShape)
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(shape = roundedCornerShape, color = color)
            .clip(roundedCornerShape)
            .clickable(onClick = onClick)
            .then(borderModifier)
    )
}