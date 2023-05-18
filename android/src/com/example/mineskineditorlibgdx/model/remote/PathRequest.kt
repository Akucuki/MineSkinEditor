package com.example.mineskineditorlibgdx.model.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PathRequest(val path: String)
