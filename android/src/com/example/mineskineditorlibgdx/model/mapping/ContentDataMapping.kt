package com.example.mineskineditorlibgdx.model.mapping

import android.net.Uri
import android.util.Log
import com.example.mineskineditorlibgdx.model.local.ContentData
import com.example.mineskineditorlibgdx.model.local.ContentItemData
import com.example.mineskineditorlibgdx.model.local.ContentItemDataUi
import com.example.mineskineditorlibgdx.model.remote.content.AddonsWithCategoriesWrapper
import com.example.mineskineditorlibgdx.model.remote.content.MyMapsWithCategoriesWrapper
import com.example.mineskineditorlibgdx.model.remote.content.RemoteContentData
import com.example.mineskineditorlibgdx.model.remote.content.SkinsWithCategoriesWrapper


fun RemoteContentData.toContentData() = ContentData(
    skins = skinsWrapper.toContentItemData(),
    maps = mapsWrapper.toContentItemData(),
    addons = addonsWrapper.toContentItemData()
)

fun ContentItemData.toContentItemDataUi(displayImagesUris: List<Uri>) = ContentItemDataUi(
    name = name,
    description = description,
    displayImagesUris = displayImagesUris
)

fun AddonsWithCategoriesWrapper.toContentItemData() : List<ContentItemData> {
    return addonsWithCategories.map { (category, addonsWithIndexesWrapper) ->
        Log.d("vitalik", "Category: $category")
        addonsWithIndexesWrapper.addonsWithIndexes.map { (_, remoteAddonData) ->
            ContentItemData(
                name = remoteAddonData.name,
                description = remoteAddonData.description,
                displayImagesPaths = remoteAddonData.displayImagesPaths,
                sourcePath = remoteAddonData.sourcePath,
                category = category
            )
        }
    }.flatten()
}

fun MyMapsWithCategoriesWrapper.toContentItemData() : List<ContentItemData> {
    return myMapsWithCategories.map { (category, mapsWithIndexesWrapper) ->
        Log.d("vitalik", "Category: $category")
        mapsWithIndexesWrapper.myMapsWithIndexes.map { (_, remoteMyMapData) ->
            ContentItemData(
                name = remoteMyMapData.name,
                description = remoteMyMapData.description,
                displayImagesPaths = remoteMyMapData.displayImagesPaths,
                sourcePath = remoteMyMapData.sourcePath,
                category = category
            )
        }
    }.flatten()
}

fun SkinsWithCategoriesWrapper.toContentItemData() : List<ContentItemData> {
    return skinsWithCategories.map { (category, skinsWithIndexesWrapper) ->
        Log.d("vitalik", "Category: $category")
        skinsWithIndexesWrapper.skinWithIndexes.map { (_, remoteSkinData) ->
            ContentItemData(
                name = "Placeholder name",//remoteSkinData.name,
                // TODO rewrite if JSON is fixed
                description = "Placeholder description",//remoteSkinData.name,
                displayImagesPaths = listOf(remoteSkinData.displayImagePath),
                sourcePath = remoteSkinData.imagePath,
                category = category
            )
        }
    }.flatten()
}