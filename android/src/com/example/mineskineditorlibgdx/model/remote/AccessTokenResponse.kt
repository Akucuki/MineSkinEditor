package com.example.mineskineditorlibgdx.model.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessTokenResponse(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "expires_in")
    val expiresIn: Long,
    @Json(name = "token_type")
    val tokenType: String,
)