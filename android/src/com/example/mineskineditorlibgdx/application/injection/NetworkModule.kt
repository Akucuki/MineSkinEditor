package com.example.mineskineditorlibgdx.application.injection

import com.example.mineskineditorlibgdx.BuildConfig
import com.example.mineskineditorlibgdx.network.AccessTokenAuthenticator
import com.example.mineskineditorlibgdx.persistence.DataStoreHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        dataStoreHandler: DataStoreHandler,
        authenticator: AccessTokenAuthenticator
    ): OkHttpClient {
        val headerInterceptor = Interceptor { chain ->
            return@Interceptor chain.proceed(
                chain.request()
                    .newBuilder()
                    .header(
                        "Authorization",
                        runBlocking { dataStoreHandler.getAccessToken() ?: "" }
                    )
                    .build()
            )
        }
        return OkHttpClient.Builder().apply {
            addInterceptor(headerInterceptor)
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(loggingInterceptor)
            }
            authenticator(authenticator)
            connectTimeout(timeout = 10, TimeUnit.SECONDS)
            writeTimeout(timeout = 10, TimeUnit.SECONDS)
            readTimeout(timeout = 10, TimeUnit.SECONDS)
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: dagger.Lazy<OkHttpClient>): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .callFactory { request -> okHttpClient.get().newCall(request) }
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

}