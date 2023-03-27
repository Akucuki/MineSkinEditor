package com.example.mineskineditorlibgdx.model

import android.content.Context
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.android.parcel.Parcelize

sealed class UiString {

    @Parcelize
    data class StringResource(
        @StringRes
        val stringId: Int
    ) : UiString(), Parcelable

    @Parcelize
    data class SimpleString(
        val string: String
    ) : UiString(), Parcelable

    @Composable
    fun asString(): String = when (this) {
        is StringResource -> stringResource(stringId)
        is SimpleString -> string
    }

    fun asString(context: Context) = when (this) {
        is StringResource -> context.getString(stringId)
        is SimpleString -> string
    }
}
