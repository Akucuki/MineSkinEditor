package com.example.mineskineditorlibgdx.features.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mineskineditorlibgdx.model.ContentTabType
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.DropboxCachingProxy
import com.example.mineskineditorlibgdx.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SELECTED_CONTENT_TYPE = "selected_content_type"
private const val CONTENT_ITEMS = "content_items"

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val navigationDispatcher: NavigationDispatcher,
    private val dropboxCachingProxy: DropboxCachingProxy
) : ViewModel() {

    val items = handle.getStateFlow(
        CONTENT_ITEMS,
        emptyList<String>()
    )

    val selectedTab = handle.getStateFlow(
        SELECTED_CONTENT_TYPE,
        ContentTabType.SKINS
    )

    init {
        // TODO remove
        items.toString()
        viewModelScope.launch(Dispatchers.IO) {
//            dropboxCachingProxy.getFilesDetailsChunk("/maps, addons, skins/content.json")
            dropboxCachingProxy.getFile("/ios – first crafty craft (out) ski toilet mods for minecraft - content - 18.07.23/crafty.json")
//            val filesDetailsChunk = dropboxCachingProxy.getFilesDetailsChunk("/maps, addons, skins")
//            Log.d("vitalik", "FilesDetailsChunk: $filesDetailsChunk")
        }
    }

    fun onTabSelected(type: ContentTabType) {
        handle[SELECTED_CONTENT_TYPE] = type

        // TODO remove
        navigationDispatcher.toString()
    }

}