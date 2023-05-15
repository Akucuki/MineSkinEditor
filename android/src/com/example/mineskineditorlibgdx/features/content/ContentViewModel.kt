package com.example.mineskineditorlibgdx.features.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mineskineditorlibgdx.model.ContentTabType
import com.example.mineskineditorlibgdx.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val SELECTED_CONTENT_TYPE = "selected_content_type"
private const val CONTENT_ITEMS = "content_items"

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val navigationDispatcher: NavigationDispatcher
) : ViewModel() {

    val items = handle.getStateFlow(
        CONTENT_ITEMS,
        emptyList<String>()
    )

    val selectedTab = handle.getStateFlow(
        SELECTED_CONTENT_TYPE,
        ContentTabType.SKINS
    )

    fun onTabSelected(type: ContentTabType) {
        handle[SELECTED_CONTENT_TYPE] = type

        // TODO remove
        navigationDispatcher.toString()
    }

}