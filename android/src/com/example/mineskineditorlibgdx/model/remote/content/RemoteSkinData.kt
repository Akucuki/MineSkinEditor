package com.example.mineskineditorlibgdx.model.remote.content

import android.os.Parcelable
import com.example.mineskineditorlibgdx.utils.CONTENT_SKIN_DISPLAY_IMAGE_PATH
import com.example.mineskineditorlibgdx.utils.CONTENT_SKIN_IMAGE_PATH
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SkinsWithCategoriesWrapper(
    val skinsWithCategories: Map<String, SkinsWithIndexesWrapper>
) : Parcelable

@Parcelize
data class SkinsWithIndexesWrapper(
    val skinWithIndexes: Map<String, RemoteSkinData>
) : Parcelable

@Parcelize
data class RemoteSkinData(
//    @SerializedName(CONTENT_SKIN_NAME)
//    val name: String,
    @SerializedName(CONTENT_SKIN_DISPLAY_IMAGE_PATH)
    val displayImagePath: String,
    @SerializedName(CONTENT_SKIN_IMAGE_PATH)
    val imagePath: String,
    // TODO return back if JSON is fixed
//    @SerializedName(CONTENT_SKIN_DESCRIPTION)
//    val description: String
) : Parcelable
