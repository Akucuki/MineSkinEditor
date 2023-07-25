package com.example.mineskineditorlibgdx.features.composables.skinEditor

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.application.theme.GrayColor
import com.example.mineskineditorlibgdx.application.theme.YellowColor
import com.example.mineskineditorlibgdx.model.EditorToolType

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    toolTypes: Array<EditorToolType>,
    selectedToolType: EditorToolType,
    onToolClick: (EditorToolType) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = GrayColor)
            .padding(top = 8.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        toolTypes.forEach { type ->
            ToolButton(
                modifier = Modifier.weight(1f),
                isActive = type == selectedToolType,
                onClick = { onToolClick(type) },
                iconId = type.iconId(),
                textId = type.nameStringId()
            )
        }
    }
}

@Composable
private fun ToolButton(
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    onClick: () -> Unit,
    @DrawableRes
    iconId: Int,
    @StringRes
    textId: Int
) {
    val contentColor = if (isActive) YellowColor else Color.White
    Column(
        modifier = modifier.fillMaxHeight().clip(CircleShape).clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(painter = painterResource(iconId), contentDescription = null, tint = contentColor)
        Text(
            text = stringResource(textId),
            style = MaterialTheme.typography.button,
            color = contentColor
        )
    }
}