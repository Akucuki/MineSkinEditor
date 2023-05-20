package com.example.mineskineditorlibgdx.persistence.dropboxCaching.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilesDetailsChunk(
    val paths: List<String>,
    val cursor: String?,
    val hasMore: Boolean,
) : Parcelable