package com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.LightPurpleColor
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun OptionSkinBlock(
    modifier: Modifier = Modifier,
    previewBitmap: ImageBitmap
) {
    Row(
        modifier = modifier
            .height(140.dp)
            .fillMaxWidth()
            .background(color = LightPurpleColor, shape = remember { RoundedCornerShape(6.dp) })
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            modifier = Modifier,
            bitmap = previewBitmap,
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OptionSkinBlockButton(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                text = "SKIN EDITOR",
                onClick = { /*TODO*/ },
                drawableId = R.drawable.img_skin_editor
            )
            OptionSkinBlockButton(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                text = "SOUND EDITOR",
                onClick = { /*TODO*/ },
                drawableId = R.drawable.ic_sound_editor
            )
        }
    }
}

@Composable
fun OptionSkinBlockButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit,
    @DrawableRes
    drawableId: Int
) {
    val roundedCornerShape = remember { RoundedCornerShape(4.dp) }
    Row(
        modifier = modifier
            .background(color = OrangeColor, shape = roundedCornerShape)
            .clip(roundedCornerShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(26.dp),
            painter = painterResource(drawableId),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
//            tint = Color.White
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 10.dp),
            style = MaterialTheme.typography.button.copy(
                fontWeight = FontWeight.W700,
                lineHeight = 20.sp
            ),
            color = Color.White
        )
    }
}