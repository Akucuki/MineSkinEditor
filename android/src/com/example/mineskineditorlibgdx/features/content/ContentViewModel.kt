package com.example.mineskineditorlibgdx.features.content

import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mineskineditorlibgdx.model.local.ContentItemDataUi
import com.example.mineskineditorlibgdx.model.local.ContentType
import com.example.mineskineditorlibgdx.model.mapping.toContentData
import com.example.mineskineditorlibgdx.model.remote.content.RemoteContentData
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.DropboxCachingProxy
import com.example.mineskineditorlibgdx.utils.NavigationDispatcher
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SELECTED_CONTENT_TYPE = "selected_content_type"
private const val CONTENT_ITEMS = "content_items"

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val navigationDispatcher: NavigationDispatcher,
    private val dropboxCachingProxy: DropboxCachingProxy,
    private val gson: Gson
) : ViewModel() {

    val isLoading = MutableStateFlow(false)
    val isLoadingError = MutableStateFlow(false)
    val contentItems = handle.getStateFlow(
        CONTENT_ITEMS,
        emptyList<ContentItemDataUi>()
    )

    val selectedTab = handle.getStateFlow(
        SELECTED_CONTENT_TYPE,
        ContentType.SKINS
    )

    init {
        loadContentItemsForType()
    }

    fun loadContentItemsForType() {
        viewModelScope.launch(Dispatchers.IO) {
            val contentCatalogFileUri = dropboxCachingProxy.getFile("/maps, addons, skins/content_with_placeholders.json")
            if (contentCatalogFileUri == null) {
                isLoadingError.value = true
                return@launch
            }
            val contentCatalogFile = contentCatalogFileUri.toFile()
            contentCatalogFile.inputStream().use { inputStream ->
                val remoteContentData = gson.fromJson(inputStream.reader(), RemoteContentData::class.java)
                val contentData = remoteContentData.toContentData()
                handle[CONTENT_ITEMS] = when (selectedTab.value) {
                    ContentType.SKINS -> contentData.skins
                    ContentType.MAPS -> contentData.maps
                    ContentType.ADDONS -> contentData.addons
                }
                Log.d("vitalik", "Content items: ${contentItems.value}")
            }
        }
    }

    fun onTabSelected(type: ContentType) {
        handle[SELECTED_CONTENT_TYPE] = type

        // TODO remove
        navigationDispatcher.toString()
    }

}