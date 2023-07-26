package com.example.mineskineditorlibgdx.features.skinEditor2D.editor.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.utils.twoFingerTransformable

// TODO probably needs some tweaking to add ability to draw bigger skins
@Composable
fun SkinCanvas(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    gridStrokeThickness: Dp = 2.dp,

    minScale: Float = .5f,
    maxScale: Float = 3f,
    minOffsetDenominator: Float = 2f,
    maxOffsetDenominator: Float = 2f
) {
    var mutableBitmap by remember(bitmap) {
        mutableStateOf(
            bitmap.copy(
                Bitmap.Config.ARGB_8888,
                true
            )
        )
    }

    var canvasSize by remember { mutableStateOf(Size.Zero) }

    var scale by remember { mutableStateOf(1f) }

    val minOffsetVertical =
        remember(canvasSize, scale) { -canvasSize.height / minOffsetDenominator * scale }
    val maxOffsetVertical =
        remember(canvasSize, scale) { canvasSize.height / maxOffsetDenominator * scale }
    val minOffsetHorizontal =
        remember(canvasSize, scale) { -canvasSize.width / minOffsetDenominator * scale }
    val maxOffsetHorizontal =
        remember(canvasSize, scale) { canvasSize.width / maxOffsetDenominator * scale }

    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(minScale, maxScale)
        val rawOffset = offset + panChange
        offset = rawOffset.copy(
            x = rawOffset.x.coerceIn(minOffsetHorizontal, maxOffsetHorizontal),
            y = rawOffset.y.coerceIn(minOffsetVertical, maxOffsetVertical)
        )
    }

    val density = LocalDensity.current
    val gridStrokeThicknessPx = with(density) { gridStrokeThickness.toPx() }

    Canvas(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
            .twoFingerTransformable(transformableState)
    ) {
        canvasSize = size

//        drawRect(
//            color = Color.Yellow,
//            topLeft = Offset(0f, 0f),
//            size = size
//        )
        val gridStrokesCount = mutableBitmap.width + 1
        val cellSize = (size.width - gridStrokesCount * gridStrokeThicknessPx) / mutableBitmap.width
        for (horizontalPixelIndex in 0 until mutableBitmap.width) {
            for (verticalPixelIndex in 0 until mutableBitmap.height) {
                val pixelColor = mutableBitmap.getPixel(horizontalPixelIndex, verticalPixelIndex)
                drawRect(
                    color = Color(pixelColor),
                    topLeft = Offset(
                        x = horizontalPixelIndex * cellSize + horizontalPixelIndex * gridStrokeThicknessPx + gridStrokeThicknessPx,
                        y = verticalPixelIndex * cellSize + verticalPixelIndex * gridStrokeThicknessPx + gridStrokeThicknessPx
                    ),
                    size = Size(cellSize, cellSize)
                )
            }
        }
//        drawGrid(
//            gridStrokesCount = gridStrokesCount,
//            gridStrokeThicknessPx = gridStrokeThicknessPx,
//            cellSize = cellSize
//        )
    }
}

private fun DrawScope.drawGrid(
    gridStrokesCount: Int,
    gridStrokeThicknessPx: Float,
    cellSize: Float
) {
    Orientation.values().forEach { orientation ->
        drawGridLines(
            orientation = orientation,
            strokesCount = gridStrokesCount,
            strokeThicknessPx = gridStrokeThicknessPx,
            spacing = cellSize
        )
    }
}

private fun DrawScope.drawGridLines(
    orientation: Orientation,
    strokesCount: Int,
    strokeThicknessPx: Float,
    spacing: Float
) {
    repeat(strokesCount) { horizontalIndex ->
        val spacedOffset =
            strokeThicknessPx * horizontalIndex + spacing * horizontalIndex + strokeThicknessPx / 2
        val startOffset = Offset(
            x = if (orientation == Orientation.Vertical) spacedOffset else 0f,
            y = if (orientation == Orientation.Vertical) 0f else spacedOffset
        )
        val endOffset = Offset(
            x = if (orientation == Orientation.Vertical) spacedOffset else size.width,
            y = if (orientation == Orientation.Vertical) size.height else spacedOffset
        )
        drawLine(
            start = startOffset,
            end = endOffset,
            color = Color.Black,
            strokeWidth = strokeThicknessPx
        )
    }
}