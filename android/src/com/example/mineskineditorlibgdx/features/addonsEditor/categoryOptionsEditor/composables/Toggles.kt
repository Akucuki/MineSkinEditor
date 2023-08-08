package com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.application.theme.LightGrayColor
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun Toggle(
    modifier: Modifier = Modifier,
    isToggled: Boolean = false,
    togglePadding: Dp = 0.dp,
    activeColor: Color,
    inactiveColor: Color,
    toggleColor: Color,
    containerShape: RoundedCornerShape,
    toggleShape: RoundedCornerShape,
) {
    val density = LocalDensity.current
    var containerSizePx by remember { mutableStateOf(IntSize.Zero) }
    val containerSize by remember(containerSizePx) {
        mutableStateOf(
            with(density) {
                DpSize(containerSizePx.width.toDp(), containerSizePx.height.toDp())
            }
        )
    }
    val toggleSize by remember(containerSize) {
        mutableStateOf(
            DpSize(
                (containerSize.width - togglePadding * 2) / 2,
                containerSize.height - togglePadding * 2
            )
        )
    }
    val toggleEndPositionX = remember(toggleSize, togglePadding) {
        toggleSize.width + togglePadding
    }

    val transition = updateTransition(isToggled, label = "toggleTransition")
    val toggleBackgroundColor by transition.animateColor(label = "toggleBackgroundColor") { isActive ->
        if (isActive) activeColor else inactiveColor
    }
    val toggleOffset by transition.animateDp(label = "toggleOffset") { isActive ->
        if (isActive) toggleEndPositionX else togglePadding
    }
    Box(
        modifier = modifier
            .background(color = toggleBackgroundColor, shape = containerShape)
            .onGloballyPositioned { containerSizePx = it.size }
            .offset(x = toggleOffset, y = togglePadding),
    ) {
        Box(
            modifier = Modifier
                .size(toggleSize)
                .background(color = toggleColor, shape = toggleShape)
        )
    }
}

@Composable
fun OptionToggle(
    modifier: Modifier = Modifier,
    isToggled: Boolean,
    onToggle: (Boolean) -> Unit,
    label: String,
    togglePadding: Dp = 4.dp,
    activeColor: Color = OrangeColor,
    inactiveColor: Color = LightGrayColor,
    toggleColor: Color = Color.Black,
    containerShape: RoundedCornerShape = remember { RoundedCornerShape(14.dp) },
    toggleShape: RoundedCornerShape = remember { RoundedCornerShape(12.dp) },
) {
    val focusManager = LocalFocusManager.current
    LabelledRow(
        label = label,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                focusManager.clearFocus()
                onToggle(!isToggled)
            },
        labelModifier = Modifier.fillMaxWidth(.5f)
    ) {
        Toggle(
            modifier = Modifier
                .size(height = 40.dp, width = 88.dp)
                .border(width = 2.dp, color = LightGrayColor, shape = containerShape),
            isToggled = isToggled,
            togglePadding = togglePadding,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            toggleColor = toggleColor,
            containerShape = containerShape,
            toggleShape = toggleShape,
        )
    }
}

@Composable
fun OptionsToggleable(
    modifier: Modifier = Modifier,
    isToggled: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = remember { RoundedCornerShape(6.dp) }),
//            .padding(start = 10.dp, end = 10.dp, top = 6.dp)
    ) {
        OptionToggle(
            isToggled = isToggled,
            onToggle = onToggle,
            label = "Player can ride"
        )
        AnimatedVisibility(visible = isToggled) {
            // TODO remove when figured out
            Column {
                repeat(4) {
                    OptionTextField(
                        modifier = Modifier.padding(start = 20.dp, bottom = 10.dp),
                        label = "Point X",
                        value = "0.000",
                        onValueChange = {}
                    )
                }
            }
        }
    }
}
