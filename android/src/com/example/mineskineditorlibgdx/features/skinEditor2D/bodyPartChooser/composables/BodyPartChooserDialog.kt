package com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.application.theme.OrangeColor
import com.example.mineskineditorlibgdx.model.BodyPartType

@Composable
fun BodyPartChooserDialog(onSelected: (BodyPartType) -> Unit, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.5f))
            .clickable(
                onClick = onDismiss,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .background(color = Color.White, shape = remember { RoundedCornerShape(16.dp) })
                .padding(20.dp)
        ) {
            items(BodyPartType.values().toList()) { bodyPartType ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { onSelected(bodyPartType) }),
                    text = stringResource(bodyPartType.partNameStringId),
                    style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.W400),
                    color = OrangeColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}