package com.example.mineskineditorlibgdx.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilesDetailsChunk(
    val paths: List<String>,
    val limit: Int = 0,
    val cursor: String?,
    val hasMore: Boolean
) : Parcelable