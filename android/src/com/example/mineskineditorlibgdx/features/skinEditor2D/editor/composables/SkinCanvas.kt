package com.example.mineskineditorlibgdx.features.skinEditor2D.editor.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.utils.calculateLineCoordinates
import com.example.mineskineditorlibgdx.utils.twoFingerTransformable

// TODO probably needs some tweaking to add ability to draw bigger skins
@Composable
fun SkinCanvas(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    gridStrokeThickness: Dp = 2.dp,
    gridStrokeColor: Color,
    areBackgroundCellsEnabled: Boolean = true,
    backgroundCellsTimesPerPixel: Int = 3,
    backgroundCellsPrimaryColor: Color = Color.LightGray,
    backgroundCellsSecondaryColor: Color = Color.Gray,

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

    val aspectRatio = remember(bitmap) { bitmap.width.toFloat() / bitmap.height }

    var canvasSize by remember { mutableStateOf(Size.Zero) }
    val gridStrokesCount = remember { mutableBitmap.width + 1 }
    var cellSize by remember { mutableStateOf(0f) }

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

    var lastPos: Offset? = remember { null }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(aspectRatio)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offset.x
                translationY = offset.y
            }
            .pointerInput(Unit) {
                detectTapGestures {
                    val x = it.x
                    val y = it.y
                    val pixelX = (x / cellSize).toInt()
                    val pixelY = (y / cellSize).toInt()
                    mutableBitmap = mutableBitmap
                        .copy(Bitmap.Config.ARGB_8888, true)
                        .apply {
                            setPixel(pixelX, pixelY, Color.Red.toArgb())
                        }
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset -> lastPos = offset },
                    onDragEnd = { lastPos = null },
                    onDragCancel = { lastPos = null },
                    onDrag = { change, _ ->
                        val currentX = (change.position.x / cellSize).toInt()
                        val currentY = (change.position.y / cellSize).toInt()
                        lastPos?.let { lastOffset ->
                            val lastX = (lastOffset.x / cellSize).toInt()
                            val lastY = (lastOffset.y / cellSize).toInt()
                            val linePixels =
                                calculateLineCoordinates(lastX, lastY, currentX, currentY)
                            mutableBitmap = mutableBitmap
                                .copy(Bitmap.Config.ARGB_8888, true)
                                .apply {
                                    linePixels.forEach { (x, y) ->
                                        if (
                                            x !in 0 until mutableBitmap.width ||
                                            y !in 0 until mutableBitmap.height
                                        ) {
                                            return@forEach
                                        }
                                        setPixel(x, y, Color.Red.toArgb())
                                    }
                                }
                        }

                        lastPos = change.position // update the last position
                        change.consume()
                    }
                )
            }
            .twoFingerTransformable(transformableState)
    ) {
        canvasSize = size
        cellSize = (canvasSize.width - gridStrokeThicknessPx) / mutableBitmap.width

        val wrappedCellSize =
            (size.width - gridStrokesCount * gridStrokeThicknessPx) / mutableBitmap.width
        drawBitmapPixels(
            bitmap = mutableBitmap,
            cellSize = wrappedCellSize,
            spacing = gridStrokeThicknessPx,
            areBackgroundCellsEnabled = areBackgroundCellsEnabled,
            backgroundCellsTimesPerPixel = backgroundCellsTimesPerPixel,
            backgroundCellsPrimaryColor = backgroundCellsPrimaryColor,
            backgroundCellsSecondaryColor = backgroundCellsSecondaryColor
        )

        drawGrid(
            gridStrokesCount = gridStrokesCount,
            gridStrokeThicknessPx = gridStrokeThicknessPx,
            cellSize = wrappedCellSize,
            color = gridStrokeColor
        )
    }
}

private fun DrawScope.drawBackgroundCells(
    timesPerPixel: Int,
    horizontalCellIndex: Int,
    size: Float,
    spacing: Float,
    verticalCellIndex: Int,
    primaryColor: Color,
    secondaryColor: Color
) {
    for (miniHorizontalIndex in 0 until timesPerPixel) {
        for (miniVerticalIndex in 0 until timesPerPixel) {
            val topLeft = Offset(
                x = (horizontalCellIndex * timesPerPixel + miniHorizontalIndex) * size + horizontalCellIndex * spacing + spacing,
                y = (verticalCellIndex * timesPerPixel + miniVerticalIndex) * size + verticalCellIndex * spacing + spacing
            )

            val globalXIndex = horizontalCellIndex * timesPerPixel + miniHorizontalIndex
            val globalYIndex = verticalCellIndex * timesPerPixel + miniVerticalIndex
            val color = if ((globalXIndex + globalYIndex) % 2 == 0) primaryColor else secondaryColor

            drawRect(
                color = color,
                topLeft = topLeft,
                size = Size(size, size)
            )
        }
    }
}

private fun DrawScope.drawBitmapPixels(
    bitmap: Bitmap,
    cellSize: Float,
    spacing: Float,
    areBackgroundCellsEnabled: Boolean = true,
    backgroundCellsTimesPerPixel: Int = 3,
    backgroundCellsPrimaryColor: Color = Color.LightGray,
    backgroundCellsSecondaryColor: Color = Color.Gray
) {
    val backgroundCellsSize = cellSize / backgroundCellsTimesPerPixel
    for (horizontalPixelIndex in 0 until bitmap.width) {
        for (verticalPixelIndex in 0 until bitmap.height) {
            if (areBackgroundCellsEnabled) {
                drawBackgroundCells(
                    timesPerPixel = backgroundCellsTimesPerPixel,
                    horizontalCellIndex = horizontalPixelIndex,
                    size = backgroundCellsSize,
                    spacing = spacing,
                    verticalCellIndex = verticalPixelIndex,
                    primaryColor = backgroundCellsPrimaryColor,
                    secondaryColor = backgroundCellsSecondaryColor
                )
            }

            val pixelColor = bitmap.getPixel(horizontalPixelIndex, verticalPixelIndex)
            drawRect(
                color = Color(pixelColor),
                topLeft = Offset(
                    x = horizontalPixelIndex * cellSize + horizontalPixelIndex * spacing + spacing,
                    y = verticalPixelIndex * cellSize + verticalPixelIndex * spacing + spacing
                ),
                size = Size(cellSize, cellSize)
            )
        }
    }
}

private fun DrawScope.drawGrid(
    gridStrokesCount: Int,
    gridStrokeThicknessPx: Float,
    cellSize: Float,
    color: Color
) {
    Orientation.values().forEach { orientation ->
        drawGridLines(
            orientation = orientation,
            strokesCount = gridStrokesCount,
            strokeThicknessPx = gridStrokeThicknessPx,
            spacing = cellSize,
            color = color
        )
    }
}

private fun DrawScope.drawGridLines(
    orientation: Orientation,
    strokesCount: Int,
    strokeThicknessPx: Float,
    spacing: Float,
    color: Color
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
            color = color,
            strokeWidth = strokeThicknessPx
        )
    }
}
