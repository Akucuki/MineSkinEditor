package com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun OptionSingleItemChooser(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    LabelledRow(label = label, modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.body2,
            )
        }
        IconButton(
            onClick = {
                focusManager.clearFocus()
                onClick()
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_three_dots),
                contentDescription = null
            )
        }
    }
}

@Composable
fun OptionMultipleItemChooser(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val roundedCornerShape = remember { RoundedCornerShape(14.dp) }
    LabelledRow(label = label, modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.body2,
            )
        }
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color = OrangeColor, shape = roundedCornerShape)
                .clip(roundedCornerShape)
                .clickable {
                    focusManager.clearFocus()
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus_bold),
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}