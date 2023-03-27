package com.example.mineskineditorlibgdx.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import kotlinx.android.parcel.Parcelize
import java.util.UUID

@Parcelize
data class ColorEntry(
    val uuid: UUID = UUID.randomUUID(),
    val color: Color
) : Parcelable
