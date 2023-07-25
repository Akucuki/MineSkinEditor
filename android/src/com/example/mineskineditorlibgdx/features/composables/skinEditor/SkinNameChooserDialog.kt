package com.example.mineskineditorlibgdx.features.composables.skinEditor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.GrayColor
import com.example.mineskineditorlibgdx.application.theme.LightOrangeColor
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun SkinNameChooserDialog(
    onDismissClick: () -> Unit,
    onSaveClick: () -> Unit,
    skinName: String,
    onSkinNameChange: (String) -> Unit,
    onClearNameClick: () -> Unit
) {
    val roundedCornerShape20 = remember { RoundedCornerShape(20.dp) }
    val roundedCornerShape8 = remember { RoundedCornerShape(8.dp) }
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
                .background(color = Color.White, shape = roundedCornerShape20)
                .clip(roundedCornerShape20)
                .clickable(
                    onClick = {},
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.choose_name_for_skin),
                style = MaterialTheme.typography.h1.copy(textAlign = TextAlign.Center),
                color = Color.Black
            )
            TextField(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                shape = roundedCornerShape8,
                value = skinName,
                onValueChange = onSkinNameChange,
                trailingIcon = {
                    IconButton(onClick = onClearNameClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null
                        )
                    }
                },
                colors = skinNameTextFieldColors()
            )
            DialogButton(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                onClick = onDismissClick,
                text = stringResource(R.string.cancel),
                isPrimary = true
            )
            DialogButton(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                onClick = onSaveClick,
                text = stringResource(R.string.save)
            )
        }
    }
}

@Composable
private fun DialogButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isPrimary: Boolean = false
) {
    val roundedCornerShape = remember { RoundedCornerShape(8.dp) }
    val textStyle = if (isPrimary) {
        MaterialTheme.typography.h1.copy(fontWeight = FontWeight.W500, lineHeight = 20.sp)
    } else {
        MaterialTheme.typography.h1.copy(lineHeight = 20.sp)
    }
    Row(
        modifier = modifier
            .background(
                color = remember { if (isPrimary) OrangeColor else GrayColor },
                shape = roundedCornerShape
            )
            .clip(roundedCornerShape)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = textStyle,
            color = Color.White
        )
    }
}

@Composable
private fun skinNameTextFieldColors() = TextFieldDefaults.textFieldColors(
    backgroundColor = LightOrangeColor,
    textColor = Color.White,
    trailingIconColor = Color.White,
    unfocusedIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
)