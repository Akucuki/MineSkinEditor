package com.example.mineskineditorlibgdx.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentItemData(
    val title: String,
    val imageUri: Uri,
    val description: String
) : Parcelable
