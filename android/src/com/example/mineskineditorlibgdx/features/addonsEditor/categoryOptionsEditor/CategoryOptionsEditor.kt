package com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.application.theme.MineSkinEditorTheme
import com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables.OptionMultipleItemChooser
import com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables.OptionSingleItemChooser
import com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables.OptionSkinBlock
import com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables.OptionTextField
import com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables.OptionToggle
import com.example.mineskineditorlibgdx.features.addonsEditor.categoryOptionsEditor.composables.OptionsToggleable
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
                        val context = LocalContext.current
                        val previewBitmap =
                            AppCompatResources.getDrawable(context, R.drawable.img_crossbow)?.toBitmapOrNull()
                        OptionSkinBlock(previewBitmap = previewBitmap!!.asImageBitmap())
                        Spacer(modifier = Modifier.height(10.dp))
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
                        Spacer(modifier = Modifier.height(10.dp))
                        OptionSingleItemChooser(
                            label = "Spawn Egg",
                            value = "pig_egg",
                            onClick = {}
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OptionMultipleItemChooser(
                            label = "Action",
                            value = "example_function",
                            onClick = {}
                        )
                    }
//                    SaveChangesDialog(
//                        onDismissClick = { /*TODO*/ },
//                        onSaveClick = { /*TODO*/ },
//                        onExitWithoutChangeClick = { /*TODO*/ },
//                        onCancelClick = {}
//                    )
//                    OptionsChooserDialog(
//                        onSelectAllClick = { /*TODO*/ },
//                        onDismissClick = { /*TODO*/ },
//                        modifier = Modifier,
//                        onDoneClick = { /*TODO*/ },
//                    )
                }
            }
        }
    }
}
