package com.example.mineskineditorlibgdx.features.libgdx.features.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.OrangeColor
import com.example.mineskineditorlibgdx.application.theme.PurpleColor
import com.example.mineskineditorlibgdx.model.AdditionalOptionsType
import com.example.mineskineditorlibgdx.model.ColorEntry
import com.example.mineskineditorlibgdx.model.EditorToolThickness

@Composable
fun RecentColorsBar(
    modifier: Modifier = Modifier,
    colorEntries: List<ColorEntry>,
    selectedColor: ColorEntry,
    onColorClick: (ColorEntry) -> Unit,
    onColorPickerClick: () -> Unit,
    onPipetteClick: () -> Unit,
    isPipetteActive: Boolean,
    onSizeChosen: (EditorToolThickness) -> Unit,
    effectValue: Float,
    onEffectChange: (Float) -> Unit,
    onEffectChangeEnded: () -> Unit,
    areAdditionalOptionsVisible: Boolean = false,
    additionalOptionsType: AdditionalOptionsType,
    selectedSize: EditorToolThickness
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (areAdditionalOptionsVisible) {
            AdditionalOptions(
                type = additionalOptionsType,
                onSizeChosen = onSizeChosen,
                onEffectChange = onEffectChange,
                onEffectChangeEnded = onEffectChangeEnded,
                effectValue = effectValue,
                selectedSize = selectedSize
            )
        } else {
            RecentColors(
                colorEntries = colorEntries,
                selectedColor = selectedColor,
                onColorClick = onColorClick,
                onColorPickerClick = onColorPickerClick,
                onPipetteClick = onPipetteClick,
                isPipetteActive = isPipetteActive
            )
        }
    }
}

@Composable
private fun AdditionalOptions(
    type: AdditionalOptionsType,
    onSizeChosen: (EditorToolThickness) -> Unit,
    onEffectChange: (Float) -> Unit,
    onEffectChangeEnded: () -> Unit,
    effectValue: Float,
    selectedSize: EditorToolThickness
) {
    if (type == AdditionalOptionsType.NONE) return

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        if (type == AdditionalOptionsType.ALL) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.effect),
                    color = OrangeColor,
                    style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.W700)
                )
                Slider(
                    modifier = Modifier.fillMaxWidth(),
                    value = effectValue,
                    onValueChange = onEffectChange,
                    valueRange = 0f..1f,
                    onValueChangeFinished = onEffectChangeEnded,
                    colors = sliderColors()
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.size),
                color = OrangeColor,
                style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.W700)
            )
            EditorToolThickness.values().forEach { size ->
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .border(
                            2.dp,
                            if (size == selectedSize) OrangeColor else Color.Black.copy(alpha = .2f),
                            CircleShape
                        )
                        .clip(CircleShape)
                        .clickable { onSizeChosen(size) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier,
                        text = size.thickness.toString(),
                        color = OrangeColor,
                        style = MaterialTheme.typography.button.copy(fontWeight = FontWeight.W700)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentColors(
    colorEntries: List<ColorEntry>,
    selectedColor: ColorEntry,
    onColorClick: (ColorEntry) -> Unit,
    onColorPickerClick: () -> Unit,
    onPipetteClick: () -> Unit,
    isPipetteActive: Boolean,
) {
    val pipetteBorderModifier = if (isPipetteActive) {
        Modifier.border(2.dp, Color.Black, CircleShape)
    } else {
        Modifier
    }
    IconButton(onClick = onColorPickerClick) {
        Icon(
            painter = painterResource(R.drawable.ic_color_picker),
            contentDescription = null,
            tint = PurpleColor
        )
    }
    colorEntries.forEach { entry ->
        ColorButton(
            modifier = Modifier.size(50.dp),
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

@Composable
fun sliderColors() = SliderDefaults.colors(
    activeTrackColor = OrangeColor,
    inactiveTrackColor = Color.Gray,
    thumbColor = OrangeColor
)
