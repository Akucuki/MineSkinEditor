package com.example.mineskineditorlibgdx.features.libgdx.features.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.*
import com.godaddy.android.colorpicker.ClassicColorPicker
import com.godaddy.android.colorpicker.HsvColor

@Composable
fun ColorPickerDialog(
    onDismissClick: () -> Unit,
    onOkClick: (Color) -> Unit,
    initialColor: Color
) {
    val roundedCornerShape20 = remember { RoundedCornerShape(20.dp) }
    var lastSelectedColor by remember { mutableStateOf(Color.White) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = .4f))
            .clickable(
                onClick = onDismissClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(
                    brush = remember {
                        Brush.verticalGradient(
                            colors = listOf(
                                ColorPickerBackgroundGradientStartColor,
                                ColorPickerBackgroundGradientEndColor
                            )
                        )
                    },
                    shape = roundedCornerShape20
                )
                .clip(roundedCornerShape20)
                .clickable(
                    onClick = {},
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClassicColorPicker(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                color = HsvColor.from(initialColor),
                onColorChanged = { lastSelectedColor = it.toColor() }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                FancyButton(
                    modifier = Modifier.weight(1f),
                    backgroundColors = listOf(
                        CancelButtonBackgroundGradientStartColor,
                        CancelButtonBackgroundGradientEndColor
                    ),
                    text = stringResource(R.string.cancel),
                    onClick = onDismissClick
                )
                FancyButton(
                    modifier = Modifier.weight(1f),
                    backgroundColors = listOf(
                        OkButtonBackgroundGradientStartColor,
                        OkButtonBackgroundGradientEndColor
                    ),
                    text = "Ok",
                    onClick = { onOkClick(lastSelectedColor) }
                )
            }
        }
    }
}

@Composable
private fun FancyButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColors: List<Color>,
    text: String
) {
    val roundedCornerShape = remember { RoundedCornerShape(12.dp) }
    Row(
        modifier = modifier
            .background(
                brush = remember { Brush.verticalGradient(colors = backgroundColors) },
                shape = roundedCornerShape
            )
            .border(
                width = 4.dp,
                color = Color.White,
                shape = roundedCornerShape
            )
            .clip(roundedCornerShape)
            .clickable(onClick = onClick)
            .padding(start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.h3,
            color = Color.White
        )
    }
}