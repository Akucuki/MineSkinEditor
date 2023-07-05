package com.example.mineskineditorlibgdx.model.local

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.mineskineditorlibgdx.R

enum class ContentType(
    @StringRes
    val textId: Int,
    @DrawableRes
    val imageId: Int,
) {
    SKINS(R.string.skins, R.drawable.img_skins),
    ADDONS(R.string.addons, R.drawable.img_addons),
    MAPS(R.string.maps, R.drawable.img_maps)
}