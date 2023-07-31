package com.example.mineskineditorlibgdx.features.skinEditor2D.bodyPartChooser.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.mineskineditorlibgdx.application.theme.GrayColor
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun SkinPreview(
    modifier: Modifier = Modifier,
    headFace: ImageBitmap,
    bodyFace: ImageBitmap,
    rightHandFace: ImageBitmap,
    leftHandFace: ImageBitmap,
    rightLegFace: ImageBitmap,
    leftLegFace: ImageBitmap,
    selectedBodyPartName: String,
    onBodyPartClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = GrayColor)
            .padding(20.dp),
    ) {
        SkinMiniFigure(
            modifier = Modifier.size(90.dp).align(Alignment.CenterStart),
            headFace = headFace,
            bodyFace = bodyFace,
            rightHandFace = rightHandFace,
            leftHandFace = leftHandFace,
            rightLegFace = rightLegFace,
            leftLegFace = leftLegFace
        )
        BodyPartFraction(
            modifier = Modifier.width(130.dp).align(Alignment.Center),
            bodyPartName = selectedBodyPartName,
            onBodyPartClick = onBodyPartClick
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .background(
                brush = remember {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f),
                            Color.Transparent,
                        )
                    )
                }
            )
    )
}

@Composable
fun SkinMiniFigure(
    modifier: Modifier = Modifier,
    headFace: ImageBitmap,
    bodyFace: ImageBitmap,
    rightHandFace: ImageBitmap,
    leftHandFace: ImageBitmap,
    rightLegFace: ImageBitmap,
    leftLegFace: ImageBitmap
) {
    val density = LocalDensity.current
    val availableSize = remember { mutableStateOf(IntSize.Zero) }
    val pixelSize = remember(availableSize.value) { availableSize.value.height / 32 }
    val pixelSizeDp = with(density) { pixelSize.toDp() }
    Column(
        modifier = modifier.onSizeChanged { availableSize.value = it },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row() {
            Image(
                modifier = Modifier.size(8 * pixelSizeDp),
                bitmap = headFace,
                contentDescription = null,
                filterQuality = FilterQuality.None
            )
        }
        Row() {
            Image(
                modifier = Modifier.size(width = 4 * pixelSizeDp, height = 12 * pixelSizeDp),
                bitmap = rightHandFace,
                contentDescription = null,
                filterQuality = FilterQuality.None
            )
            Image(
                modifier = Modifier.size(width = 8 * pixelSizeDp, height = 12 * pixelSizeDp),
                bitmap = bodyFace,
                contentDescription = null,
                filterQuality = FilterQuality.None
            )
            Image(
                modifier = Modifier.size(width = 4 * pixelSizeDp, height = 12 * pixelSizeDp),
                bitmap = leftHandFace,
                contentDescription = null,
                filterQuality = FilterQuality.None
            )
        }
        Row() {
            Image(
                modifier = Modifier.size(width = 4 * pixelSizeDp, height = 12 * pixelSizeDp),
                bitmap = rightLegFace,
                contentDescription = null,
                filterQuality = FilterQuality.None
            )
            Image(
                modifier = Modifier.size(width = 4 * pixelSizeDp, height = 12 * pixelSizeDp),
                bitmap = leftLegFace,
                contentDescription = null,
                filterQuality = FilterQuality.None
            )
        }
    }
}

@Composable
private fun BodyPartFraction(
    modifier: Modifier = Modifier,
    bodyPartName: String,
    onBodyPartClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.clickable(onClick = onBodyPartClick),
            text = bodyPartName,
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.W400),
            color = OrangeColor
        )
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.White)
        Text(
            text = "Part of skin",
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.W400),
            color = Color.White
        )
    }
}