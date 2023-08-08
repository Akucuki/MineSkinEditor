package com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LabelledRow(
    modifier: Modifier = Modifier,
    labelModifier: Modifier = Modifier,
    label: String,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .background(color = Color.White, shape = remember { RoundedCornerShape(6.dp) })
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = labelModifier.padding(end = 16.dp),
            text = label,
            style = MaterialTheme.typography.body2
        )
        content()
    }
}