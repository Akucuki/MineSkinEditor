package com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.application.theme.CancelButtonBackgroundGradientEndColor
import com.example.mineskineditorlibgdx.application.theme.LightGrayColor
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun OptionTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val backgroundColor = remember(isFocused) {
        if (isFocused) CancelButtonBackgroundGradientEndColor else LightGrayColor
    }
    LabelledRow(
        modifier = modifier.fillMaxWidth(),
        label = label,
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(.8f),
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center),
            singleLine = true,
            cursorBrush = remember { SolidColor(OrangeColor) },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = backgroundColor,
                            shape = remember { RoundedCornerShape(4.dp) })
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            },
            interactionSource = interactionSource,
        )
    }
}

@Composable
fun OptionsChooserDialogSearchField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center),
        singleLine = true,
        cursorBrush = remember { SolidColor(OrangeColor) },
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = remember { RoundedCornerShape(6.dp) }
                    )
                    .padding(vertical = 6.dp, horizontal = 12.dp),
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                innerTextField()
            }
        }
    )
}