package com.example.mineskineditorlibgdx.model.remote
import android.os.Parcelable

import com.squareup.moshi.JsonClass

import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@JsonClass(generateAdapter = true)
@Parcelize
data class ContentData(
//    @Json(name = "avc")
//    val avc: Avc,
    @Json(name = "Favorites")
    val favorites: Favorites,
//    @Json(name = "0fsps-")
//    val fsps: String,
//    @Json(name = "_g9h4jyr6")
//    val g9h4jyr6: String,
    @Json(name = "8m5sz")
    val mapsData: MapsData,
    @Json(name = "t2r")
    val addonsData: AddonsData,
//    @Json(name = "t3hv982wo")
//    val t3hv982wo: String,
//    @Json(name = "6u_7cfw1i")
//    val u7cfw1i: String,
//    @Json(name = "8ux-8qy3")
//    val ux8qy3: String,
//    @Json(name = "3v0k-v_")
//    val v0kV: String,
    @Json(name = "6y37wmxowk")
    val skinsData: SkinsData
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
class Favorites() : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class MapsData(
    val categorizedMapsData: Map<String, >
) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class MapData(
    @Json(name = "a7cg")
    val name: String,
    @Json(name = "vrl3")
    val description: String,

)

