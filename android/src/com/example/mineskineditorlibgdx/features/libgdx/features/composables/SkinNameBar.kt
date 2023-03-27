package com.example.mineskineditorlibgdx.features.libgdx.features.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.application.theme.GrayColor
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun SkinNameBar(
    modifier: Modifier = Modifier, name: String, onNameChange: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp)
            .background(color = GrayColor),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = name,
            onValueChange = onNameChange,
            textStyle = MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center),
            colors = skinNameTextFieldColors(),
            singleLine = true,
            maxLines = 1
        )
    }
}

@Composable
private fun skinNameTextFieldColors() = TextFieldDefaults.textFieldColors(
    textColor = OrangeColor,
    backgroundColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    cursorColor = OrangeColor,

)