package com.example.mineskineditorlibgdx.features.libgdx.features.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.mineskineditorlibgdx.model.ColorEntry

@Composable
fun RecentColorsBar(
    modifier: Modifier = Modifier,
    colorEntries: List<ColorEntry>,
    selectedColor: ColorEntry,
    onColorClick: (ColorEntry) -> Unit,
    onColorPickerClick: () -> Unit,
    onPipetteClick: () -> Unit,
    isPipetteActive: Boolean
) {
    val pipetteBorderModifier = if (isPipetteActive) {
        Modifier.border(2.dp, Color.Black, CircleShape)
    } else {
        Modifier
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = Color.White)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onColorPickerClick) {
            Icon(
                painter = painterResource(R.drawable.ic_color_picker),
                contentDescription = null,
                tint = PurpleColor
            )
        }
        colorEntries.forEach { entry ->
            ColorButton(
                color = entry.color,
                onClick = { onColorClick(entry) },
                isSelected = entry.uuid == selectedColor.uuid
            )
        }
        IconButton(onClick = onPipetteClick, modifier = pipetteBorderModifier) {
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
        Modifier.border(
            width = 0.dp,
            color = Color.Black.copy(alpha = .4f),
            shape = roundedCornerShape
        )
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