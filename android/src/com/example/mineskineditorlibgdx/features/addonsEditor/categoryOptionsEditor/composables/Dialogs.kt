package com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.GrayColor
import com.example.mineskineditorlibgdx.application.theme.LightGrayColor
import com.example.mineskineditorlibgdx.application.theme.OrangeColor

@Composable
fun SaveChangesDialog(
    modifier: Modifier = Modifier,
    onDismissClick: () -> Unit,
    onSaveClick: () -> Unit,
    onExitWithoutChangeClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    val roundedCornerShape = remember { RoundedCornerShape(8.dp) }
    Box(
        modifier = modifier
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
                .padding(horizontal = 24.dp)
                .background(color = Color.White, shape = roundedCornerShape)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.save_changes_before_exiting),
                style = MaterialTheme.typography.body2
            )
            DialogButton(
                onClick = onSaveClick,
                text = stringResource(R.string.save),
                isPrimary = true
            )
            DialogButton(
                onClick = onExitWithoutChangeClick,
                text = stringResource(R.string.exit_without_change)
            )
            DialogButton(onClick = onCancelClick, text = stringResource(R.string.cancel))
        }
    }
}

@Composable
private fun DialogButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean = false,
) {
    val roundedCornerShape = remember { RoundedCornerShape(8.dp) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = remember { if (isPrimary) OrangeColor else GrayColor },
                shape = roundedCornerShape
            )
            .clip(roundedCornerShape)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OptionsChooserDialog(
    modifier: Modifier = Modifier,
    onSelectAllClick: () -> Unit,
    onDismissClick: () -> Unit,
    onDoneClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = .6f))
            .clickable(
                onClick = onDismissClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp).systemBarsPadding(),
        ) {
            OptionsChooserDialogSearchField(
                value = "Cachalote",
                onValueChange = {},
            )
            LazyVerticalStaggeredGrid(
                modifier = Modifier.weight(1f),
                columns = remember { StaggeredGridCells.Fixed(2) },
                verticalItemSpacing = 10.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = remember { PaddingValues(top = 10.dp, bottom = 10.dp) }
            ) {
                items(45) {
                    SelectableButton(
                        text = "Test $it",
                        isSelected = it % 3 == 0,
                        onClick = {}
                    )
                }
            }
            OptionsChooserDialogButton(
                modifier = Modifier.fillMaxWidth().height(40.dp),
                text = stringResource(R.string.select_all),
                onClick = onSelectAllClick
            )
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OptionsChooserDialogButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.cancel),
                    onClick = onDismissClick
                )
                OptionsChooserDialogButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.done),
                    onClick = onDoneClick
                )
            }
        }
    }
}

@Composable
fun SelectableButton(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = selectableButtonColors(isSelected),
        shape = remember { RoundedCornerShape(6.dp) }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.W500)
        )
    }
}

@Composable
fun selectableButtonColors(isSelected: Boolean) = ButtonDefaults.textButtonColors(
    backgroundColor = if (isSelected) Color.White else LightGrayColor,
    contentColor = if (isSelected) LightGrayColor else Color.White
)

@Composable
fun OptionsChooserDialogButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = remember { RoundedCornerShape(6.dp) },
        border = remember { BorderStroke(width = 2.dp, color = Color.Black) },
        colors = optionsChooserDialogButtonColors()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
        )
    }
}

@Composable
fun optionsChooserDialogButtonColors() = ButtonDefaults.textButtonColors(
    backgroundColor = Color.White,
    contentColor = Color.Black
)