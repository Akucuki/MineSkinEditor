package com.example.mineskineditorlibgdx.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.android.parcel.Parcelize
import java.util.UUID

data class ColorEntry(
    val uuid: UUID = UUID.randomUUID(),
    val color: Color
) {
    fun toParcelableColorEntry() = ParcelableColorEntry(
        uuid = uuid,
        colorValue = color.toArgb()
    )
}

@Parcelize
data class ParcelableColorEntry(
    val uuid: UUID,
    val colorValue: Int
) : Parcelable {

    fun toColorEntry() = ColorEntry(
        uuid = uuid,
        color = Color(colorValue)
    )
}

fun List<ParcelableColorEntry>.toColorEntries() = map { it.toColorEntry() }
