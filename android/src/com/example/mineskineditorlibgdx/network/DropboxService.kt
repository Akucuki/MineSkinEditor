package com.example.mineskineditorlibgdx.network

import com.example.mineskineditorlibgdx.model.remote.AccessTokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DropboxService {

    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun accessToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): AccessTokenResponse


}