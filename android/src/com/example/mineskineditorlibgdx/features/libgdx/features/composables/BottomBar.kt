package com.example.mineskineditorlibgdx.features.libgdx.features.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.mineskineditorlibgdx.model.EditorToolWrapper

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    tools: List<EditorToolWrapper>,
    onToolClick: (EditorToolWrapper) -> Unit
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
        tools.forEach { tool ->
            ToolButton(
                modifier = Modifier.weight(1f),
                isActive = tool.isActive,
                onClick = { onToolClick(tool) },
                iconId = tool.type.iconId(),
                textId = tool.type.nameStringId()
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