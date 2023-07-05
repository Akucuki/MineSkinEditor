package com.example.mineskineditorlibgdx.model.local

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentData(
    val skins: List<ContentItemData>,
    val maps: List<ContentItemData>,
    val addons: List<ContentItemData>
) : Parcelable

@Parcelize
data class ContentItemData(
    val name: String,
    val description: String,
    val displayImagesPaths: List<String>,
    val sourcePath: String,
    val category: String
) : Parcelable

@Parcelize
data class ContentItemDataUi(
    val name: String,
    val description: String,
    val displayImagesUris: List<Uri>
) : Parcelable


