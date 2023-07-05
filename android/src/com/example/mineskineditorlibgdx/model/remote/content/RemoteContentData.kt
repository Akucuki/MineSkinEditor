package com.example.mineskineditorlibgdx.model.remote.content

import android.os.Parcelable
import com.example.mineskineditorlibgdx.utils.CONTENT_ADDONS_OBJECT_WRAPPER_NAME
import com.example.mineskineditorlibgdx.utils.CONTENT_MAPS_OBJECT_WRAPPER_NAME
import com.example.mineskineditorlibgdx.utils.CONTENT_SKINS_OBJECT_WRAPPER_NAME
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RemoteContentData(
    @SerializedName(CONTENT_SKINS_OBJECT_WRAPPER_NAME)
    val skinsWrapper: SkinsWithCategoriesWrapper,
    @SerializedName(CONTENT_MAPS_OBJECT_WRAPPER_NAME)
    val mapsWrapper: MyMapsWithCategoriesWrapper,
    @SerializedName(CONTENT_ADDONS_OBJECT_WRAPPER_NAME)
    val addonsWrapper: AddonsWithCategoriesWrapper
) : Parcelable
