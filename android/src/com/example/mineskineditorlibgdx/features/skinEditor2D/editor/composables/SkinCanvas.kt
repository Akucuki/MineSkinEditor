package com.example.mineskineditorlibgdx.features.skinEditor2D.editor.composables

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// TODO probably needs some tweaking to add ability to draw bigger skins
@Composable
fun SkinCanvas(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    gridStrokeThickness: Dp = 2.dp,
) {
    val heightInPixels = bitmap.height
    val widthInPixels = bitmap.width
    var mutableBitmap by remember { mutableStateOf(bitmap.copy(Bitmap.Config.ARGB_8888, true)) }
    val density = LocalDensity.current
    val strokeThickness = remember { with(density) { gridStrokeThickness.toPx() } }
    var canvasWidth by remember { mutableStateOf(0) }
    val canvasPixelWidth by remember(canvasWidth) { mutableStateOf(canvasWidth / widthInPixels.toFloat()) }

    Canvas(
        modifier = modifier
            .onSizeChanged { canvasWidth = it.width }
            .pointerInput(Unit) {
                detectTapGestures {
                    val localCanvasPixelWidth = canvasWidth / widthInPixels.toFloat()
                    val x = (it.x / localCanvasPixelWidth).toInt()
                    val y = (it.y / localCanvasPixelWidth).toInt()
                    mutableBitmap = mutableBitmap
                        .copy(Bitmap.Config.ARGB_8888, true)
                        .apply { setPixel(x, y, Color.Red.toArgb()) }
                }
            },
        onDraw = {
            val canvasInnerPixelWidth = canvasPixelWidth - 4
            for (x in 0 until widthInPixels) {
                for (y in 0 until heightInPixels) {
                    drawOutlinedPixel(
                        bitmap = mutableBitmap,
                        x = x,
                        y = y,
                        pixelWidth = canvasPixelWidth,
                        innerPixelWidth = canvasInnerPixelWidth,
                        outlineThickness = strokeThickness
                    )
                }
            }
        }
    )
}

private fun DrawScope.drawOutlinedPixel(
    x: Int,
    y: Int,
    bitmap: Bitmap,
    pixelWidth: Float,
    innerPixelWidth: Float,
    outlineThickness: Float
) {
    val pixelColor = bitmap.getPixel(x, y)
    val offset = Offset(x * pixelWidth, y * pixelWidth)
    drawRect(
        color = Color(pixelColor),
        topLeft = offset,
        size = Size(innerPixelWidth, innerPixelWidth)
    )
    drawRect(
        color = Color.White,
        topLeft = offset,
        size = Size(pixelWidth, pixelWidth),
        style = Stroke(width = outlineThickness)
    )
}
