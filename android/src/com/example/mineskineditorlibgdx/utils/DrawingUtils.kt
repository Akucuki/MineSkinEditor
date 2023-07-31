package com.example.mineskineditorlibgdx.utils

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.IntOffset
import com.example.mineskineditorlibgdx.model.BodyPartType
import com.example.mineskineditorlibgdx.model.FaceCoordinates
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.abs

fun getBodyPartFaceCoordinates(
    bodyPartType: BodyPartType,
    faceType: BodyPartType.FaceType
): FaceCoordinates {
    val top = when (bodyPartType) {
        BodyPartType.HEAD -> FaceCoordinates(8, 0, 8, 8)
        BodyPartType.BODY -> FaceCoordinates(20, 16, 8, 4)
        BodyPartType.RIGHT_ARM -> FaceCoordinates(44, 16, 4, 4)
        BodyPartType.LEFT_ARM -> FaceCoordinates(36, 48, 4, 4)
        BodyPartType.RIGHT_LEG -> FaceCoordinates(4, 16, 4, 4)
        BodyPartType.LEFT_LEG -> FaceCoordinates(20, 48, 4, 4)
    }
    val right = when (bodyPartType) {
        BodyPartType.HEAD -> FaceCoordinates(16, 8, 8, 8)
        BodyPartType.BODY -> FaceCoordinates(28, 20, 4, 12)
        BodyPartType.RIGHT_ARM -> FaceCoordinates(40, 20, 4, 12)
        BodyPartType.LEFT_ARM -> FaceCoordinates(40, 52, 4, 12)
        BodyPartType.RIGHT_LEG -> FaceCoordinates(8, 20, 4, 12)
        BodyPartType.LEFT_LEG -> FaceCoordinates(24, 52, 4, 12)
    }
    val front = when (bodyPartType) {
        BodyPartType.HEAD -> FaceCoordinates(8, 8, 8, 8)
        BodyPartType.BODY -> FaceCoordinates(20, 20, 8, 12)
        BodyPartType.RIGHT_ARM -> FaceCoordinates(44, 20, 4, 12)
        BodyPartType.LEFT_ARM -> FaceCoordinates(36, 52, 4, 12)
        BodyPartType.RIGHT_LEG -> FaceCoordinates(4, 20, 4, 12)
        BodyPartType.LEFT_LEG -> FaceCoordinates(20, 52, 4, 12)
    }
    val left = when (bodyPartType) {
        BodyPartType.HEAD -> FaceCoordinates(0, 8, 8, 8)
        BodyPartType.BODY -> FaceCoordinates(16, 20, 4, 12)
        BodyPartType.RIGHT_ARM -> FaceCoordinates(40, 20, 4, 12)
        BodyPartType.LEFT_ARM -> FaceCoordinates(32, 52, 4, 12)
        BodyPartType.RIGHT_LEG -> FaceCoordinates(0, 20, 4, 12)
        BodyPartType.LEFT_LEG -> FaceCoordinates(16, 52, 4, 12)
    }
    val back = when (bodyPartType) {
        BodyPartType.HEAD -> FaceCoordinates(24, 8, 8, 8)
        BodyPartType.BODY -> FaceCoordinates(32, 20, 8, 12)
        BodyPartType.RIGHT_ARM -> FaceCoordinates(52, 20, 4, 12)
        BodyPartType.LEFT_ARM -> FaceCoordinates(44, 52, 4, 12)
        BodyPartType.RIGHT_LEG -> FaceCoordinates(12, 20, 4, 12)
        BodyPartType.LEFT_LEG -> FaceCoordinates(28, 52, 4, 12)
    }
    val bottom = when (bodyPartType) {
        BodyPartType.HEAD -> FaceCoordinates(16, 0, 8, 8)
        BodyPartType.BODY -> FaceCoordinates(28, 16, 8, 4)
        BodyPartType.RIGHT_ARM -> FaceCoordinates(48, 16, 4, 4)
        BodyPartType.LEFT_ARM -> FaceCoordinates(40, 48, 4, 4)
        BodyPartType.RIGHT_LEG -> FaceCoordinates(8, 16, 4, 4)
        BodyPartType.LEFT_LEG -> FaceCoordinates(24, 48, 4, 4)
    }
    val faceCoordinates = when (faceType) {
        BodyPartType.FaceType.TOP -> top
        BodyPartType.FaceType.RIGHT -> right
        BodyPartType.FaceType.FRONT -> front
        BodyPartType.FaceType.LEFT -> left
        BodyPartType.FaceType.BACK -> back
        BodyPartType.FaceType.BOTTOM -> bottom
    }
    return faceCoordinates
}

fun Modifier.twoFingerTransformable(
    state: TransformableState,
    lockRotationOnZoomPan: Boolean = false,
    enabled: Boolean = true
) = composed(
    factory = {
        val updatePanZoomLock = rememberUpdatedState(lockRotationOnZoomPan)
        val channel = remember { Channel<TransformEvent>(capacity = Channel.UNLIMITED) }
        if (enabled) {
            LaunchedEffect(state) {
                while (isActive) {
                    var event = channel.receive()
                    if (event !is TransformEvent.TransformStarted) continue
                    try {
                        state.transform(MutatePriority.UserInput) {
                            while (event !is TransformEvent.TransformStopped) {
                                (event as? TransformEvent.TransformDelta)?.let {
                                    transformBy(it.zoomChange, it.panChange, it.rotationChange)
                                }
                                event = channel.receive()
                            }
                        }
                    } catch (_: CancellationException) {
                        // ignore the cancellation and start over again.
                    }
                }
            }
        }
        val block: suspend PointerInputScope.() -> Unit = remember {
            {
                coroutineScope {
                    awaitEachGesture {
                        try {
                            detectZoom(updatePanZoomLock, channel)
                        } catch (exception: CancellationException) {
                            if (!isActive) throw exception
                        } finally {
                            channel.trySend(TransformEvent.TransformStopped)
                        }
                    }
                }
            }
        }
        if (enabled) Modifier.pointerInput(Unit, block) else Modifier
    },
    inspectorInfo = debugInspectorInfo {
        name = "transformable"
        properties["state"] = state
        properties["enabled"] = enabled
        properties["lockRotationOnZoomPan"] = lockRotationOnZoomPan
    }
)

private sealed class TransformEvent {
    object TransformStarted : TransformEvent()
    object TransformStopped : TransformEvent()
    class TransformDelta(
        val zoomChange: Float,
        val panChange: Offset,
        val rotationChange: Float
    ) : TransformEvent()
}

private suspend fun AwaitPointerEventScope.detectZoom(
    panZoomLock: State<Boolean>,
    channel: Channel<TransformEvent>
) {
    var rotation = 0f
    var zoom = 1f
    var pan = Offset.Zero
    var pastTouchSlop = false
    val touchSlop = viewConfiguration.touchSlop
    var lockedToPanZoom = false
    awaitFirstDown(requireUnconsumed = false)
    do {
        val event = awaitPointerEvent()
        val canceled = event.changes.any { it.isConsumed }
        if (!canceled) {
            val zoomChange = event.calculateZoom()
            val rotationChange = event.calculateRotation()
            val panChange = if (event.changes.count() > 1) event.calculatePan() else Offset.Zero

            if (!pastTouchSlop) {
                zoom *= zoomChange
                rotation += rotationChange
                pan += panChange

                val centroidSize = event.calculateCentroidSize(useCurrent = false)
                val zoomMotion = abs(1 - zoom) * centroidSize
                val rotationMotion = abs(rotation * PI.toFloat() * centroidSize / 180f)
                val panMotion = pan.getDistance()

                if (zoomMotion > touchSlop ||
                    rotationMotion > touchSlop ||
                    panMotion > touchSlop
                ) {
                    pastTouchSlop = true
                    lockedToPanZoom = panZoomLock.value && rotationMotion < touchSlop
                    channel.trySend(TransformEvent.TransformStarted)
                }
            }

            if (pastTouchSlop) {
                val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
                if (effectiveRotation != 0f ||
                    zoomChange != 1f ||
                    panChange != Offset.Zero
                ) {
                    channel.trySend(
                        TransformEvent.TransformDelta(
                            zoomChange,
                            panChange,
                            effectiveRotation
                        )
                    )
                }
                event.changes.forEach {
                    if (it.positionChanged()) {
                        it.consume()
                    }
                }
            }
        }
    } while (!canceled && event.changes.any { it.pressed })
}

fun calculateLineCoordinates(x0: Int, y0: Int, x1: Int, y1: Int): List<IntOffset> {
    val pixels = mutableListOf<IntOffset>()

    val dx = abs(x1 - x0)
    val dy = abs(y1 - y0)

    val sx = if (x0 < x1) 1 else -1
    val sy = if (y0 < y1) 1 else -1

    var err = (if (dx > dy) dx else -dy) / 2
    var err2: Int

    var x = x0
    var y = y0
    while (true) {
        pixels.add(IntOffset(x, y))

        if (x == x1 && y == y1) break

        err2 = err
        if (err2 > -dx) {
            err -= dy
            x += sx
        }
        if (err2 < dy) {
            err += dx
            y += sy
        }
    }
    return pixels
}
