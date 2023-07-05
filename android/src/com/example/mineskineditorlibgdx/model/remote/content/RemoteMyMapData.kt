package com.example.mineskineditorlibgdx.model.remote.content

import android.os.Parcelable
import com.example.mineskineditorlibgdx.utils.CONTENT_MAP_DESCRIPTION
import com.example.mineskineditorlibgdx.utils.CONTENT_MAP_DISPLAY_IMAGES_PATHS
import com.example.mineskineditorlibgdx.utils.CONTENT_MAP_NAME
import com.example.mineskineditorlibgdx.utils.CONTENT_MAP_SOURCE_PATH
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyMapsWithCategoriesWrapper(
    val myMapsWithCategories: Map<String, MyMapsWithIndexesWrapper>
) : Parcelable

@Parcelize
data class MyMapsWithIndexesWrapper(
    val myMapsWithIndexes: Map<String, RemoteMyMapData>
) : Parcelable

@Parcelize
data class RemoteMyMapData(
    @SerializedName(CONTENT_MAP_NAME)
    val name: String,
    @SerializedName(CONTENT_MAP_DESCRIPTION)
    val description: String,
    @SerializedName(CONTENT_MAP_DISPLAY_IMAGES_PATHS)
    val displayImagesPaths: List<String>,
    @SerializedName(CONTENT_MAP_SOURCE_PATH)
    val sourcePath: String
) : Parcelable