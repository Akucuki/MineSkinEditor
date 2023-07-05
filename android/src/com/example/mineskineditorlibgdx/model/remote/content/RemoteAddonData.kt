package com.example.mineskineditorlibgdx.model.remote.content

import android.os.Parcelable
import com.example.mineskineditorlibgdx.utils.CONTENT_ADDON_DESCRIPTION
import com.example.mineskineditorlibgdx.utils.CONTENT_ADDON_DISPLAY_IMAGES_PATHS
import com.example.mineskineditorlibgdx.utils.CONTENT_ADDON_NAME
import com.example.mineskineditorlibgdx.utils.CONTENT_ADDON_SOURCE_PATH
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddonsWithCategoriesWrapper(
    val addonsWithCategories: Map<String, AddonsWithIndexesWrapper>
) : Parcelable

@Parcelize
data class AddonsWithIndexesWrapper(
    val addonsWithIndexes: Map<String, RemoteAddonData>
) : Parcelable

@Parcelize
data class RemoteAddonData(
    @SerializedName(CONTENT_ADDON_NAME)
    val name: String,
    @SerializedName(CONTENT_ADDON_DESCRIPTION)
    val description: String,
    @SerializedName(CONTENT_ADDON_DISPLAY_IMAGES_PATHS)
    val displayImagesPaths: List<String>,
    @SerializedName(CONTENT_ADDON_SOURCE_PATH)
    val sourcePath: String
) : Parcelable
