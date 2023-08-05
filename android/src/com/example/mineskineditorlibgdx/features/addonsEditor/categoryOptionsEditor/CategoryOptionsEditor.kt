package com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.CancelButtonBackgroundGradientEndColor
import com.example.mineskineditorlibgdx.application.theme.LightGrayColor
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.application.theme.OrangeColor
import com.example.mineskineditorlibgdx.features.composables.TopAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryOptionsEditor : Fragment() {

    private val viewModel: CategoryOptionsEditorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {


        setContent {
            MineSkinEditorTheme {
                Box {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        contentDescription = null,
                        painter = painterResource(R.drawable.bg_main),
                        contentScale = ContentScale.FillBounds
                    )
                    TopAppBar(
                        trailingIconId = R.drawable.ic_search,
                        onTrailingButtonClick = { /*TODO*/ },
                        title = "Categories"
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(top = 48.dp)
                            .padding(horizontal = 8.dp)
                    ) {
                        var isActive by remember { mutableStateOf(false) }
                        var isOptionsToggleableActive by remember { mutableStateOf(false) }
                        OptionTextField(label = "Name", value = "armor stand", onValueChange = {})
                        Spacer(modifier = Modifier.height(10.dp))
                        OptionToggle(
                            label = "Change Material",
                            isToggled = isActive,
                            onToggle = { isActive = it }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OptionsToggleable(
                            isToggled = isOptionsToggleableActive,
                            onToggle = { isOptionsToggleableActive = it }
                        )
                    }
                }
            }
        }
    }
}

// TODO each option should have some unique id

@Composable
fun LabelledRow(
    modifier: Modifier = Modifier,
    labelModifier: Modifier = Modifier,
    label: String,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .background(color = Color.White, shape = remember { RoundedCornerShape(6.dp) })
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = labelModifier.padding(end = 16.dp),
            text = label,
            style = MaterialTheme.typography.body2
        )
        content()
    }
}

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
fun OptionTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val backgroundColor = remember(isFocused) {
        if (isFocused) CancelButtonBackgroundGradientEndColor else LightGrayColor
    }
    LabelledRow(
        modifier = modifier,
        label = label,
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.body1.copy(
                lineHeight = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
            ),
            singleLine = true,
            cursorBrush = remember { SolidColor(OrangeColor) },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = backgroundColor,
                            shape = remember { RoundedCornerShape(4.dp) })
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    innerTextField()
                }
            },
            interactionSource = interactionSource,
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
            modifier = Modifier.padding(top = 6.dp, start = 10.dp, end = 10.dp),
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
