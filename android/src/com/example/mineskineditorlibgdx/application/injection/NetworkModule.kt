package com.example.mineskineditorlibgdx.application.injection

import android.content.Context
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.oauth.DbxCredential
import com.dropbox.core.v2.DbxClientV2
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.persistence.DataStoreHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

//    @Singleton
//    @Provides
//    fun provideOkHttpClient(
//        @ApplicationContext
//        context: Context,
////        dataStoreHandler: DataStoreHandler,
//        authenticator: AccessTokenAuthenticator
//    ): OkHttpClient {
////        val cache = getCache(context)
//        val cachingInterceptor = Interceptor { chain ->
//            return@Interceptor chain.proceed(
//                chain.request().newBuilder()
////                    .removeHeader("Cache-Control")
////                    .addHeader("Cache-Control", "public, max-age=360000")
////                    .cacheControl(
////                        CacheControl.Builder()
////                            .maxStale(CACHE_MAX_STALE_DAYS, TimeUnit.DAYS)
////                            .build()
////                    )
////                    .header(
////                        "Authorization",
////                        runBlocking { dataStoreHandler.getAccessToken() ?: "" }
////                    )
//                    .build()
//            )
//        }
//        return OkHttpClient.Builder().apply {
//            addInterceptor(cachingInterceptor)
//            if (BuildConfig.DEBUG) {
//                val loggingInterceptor = HttpLoggingInterceptor().apply {
//                    level = HttpLoggingInterceptor.Level.BODY
//                }
//                addInterceptor(loggingInterceptor)
//            }
//            authenticator(authenticator)
////            cache(cache)
//            connectTimeout(timeout = 10, TimeUnit.SECONDS)
//            writeTimeout(timeout = 10, TimeUnit.SECONDS)
//            readTimeout(timeout = 10, TimeUnit.SECONDS)
//        }.build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideRetrofit(okHttpClient: dagger.Lazy<OkHttpClient>): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(BuildConfig.BASE_URL)
//            .callFactory { request -> okHttpClient.get().newCall(request) }
//            .addConverterFactory(MoshiConverterFactory.create())
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideDropboxService(retrofit: Retrofit): DropboxService {
//        return retrofit.create(DropboxService::class.java)
//    }
//
//    private fun getCache(context: Context): Cache {
//        val cacheDirectory = File(context.cacheDir, context.getString(R.string.cache_dir_name))
//        if (!cacheDirectory.exists()) cacheDirectory.mkdirs()
//        return Cache(cacheDirectory, MAX_CACHE_SIZE)
//    }

    @Provides
    @Singleton
    fun provideDropboxClient(
        @ApplicationContext
        context: Context,
        dataStoreHandler: DataStoreHandler,
//        dropboxService: dagger.Lazy<DropboxService>
    ): DbxClientV2 {
        val appKey = context.getString(R.string.dropbox_app_key)
        val appSecret = context.getString(R.string.dropbox_app_secret)
        val refreshToken = context.getString(R.string.dropbox_refresh_token)
        return runBlocking(Dispatchers.IO) {
//            val accessToken = dataStoreHandler.getAccessToken()
            val credentialString = dataStoreHandler.getDbxCredential()
            val credential = if (credentialString != null) {
                DbxCredential.Reader.readFully(credentialString)
            } else {
                // TODO Needs to be tested thoroughly
//                val accessTokenResponse = dropboxService.get().accessToken(
//                    refreshToken = refreshToken,
//                    clientId = appKey,
//                    clientSecret = appSecret
//                )
//                DbxCredential(
//                    accessTokenResponse.accessToken,
//                    accessTokenResponse.expiresIn,
//                    refreshToken,
//                    appKey,
//                    appSecret
//                )
                DbxCredential(
                    "",
                    0,
                    refreshToken,
                    appKey,
                    appSecret
                )
            }
            dataStoreHandler.setDbxCredential(credential.toString())
            val requestConfig = DbxRequestConfig.newBuilder(context.getString(R.string.app_name))
                .withAutoRetryEnabled()
                .build()
            val client = DbxClientV2(requestConfig, credential).apply {
                // Just to make sure the credentials are updated
                files().listFolder("")
            }
            dataStoreHandler.setDbxCredential(credential.toString())
            client
        }
    }

//    @Provides
//    @Singleton
//    fun provideDropboxCachingProxy(
//        @ApplicationContext
//        context: Context,
//        dbxClientV2: DbxClientV2,
//        dataStoreHandler: DataStoreHandler,
//    ): DropboxCachingProxy {
//        return DropboxCachingProxy(context, dbxClientV2, dataStoreHandler)
//    }

}