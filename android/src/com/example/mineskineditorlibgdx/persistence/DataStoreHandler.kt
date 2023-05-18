package com.example.mineskineditorlibgdx.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.getDataStore: DataStore<Preferences> by preferencesDataStore(name = "mineSkinEditorDataStore")

class DataStoreHandler @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.getDataStore

//    suspend fun setTokens(accessToken: String, refreshToken: String) {
//        dataStore.edit { preferences ->
//            preferences[PreferencesKeys.ACCESS_TOKEN] = "Bearer $accessToken"
//            preferences[PreferencesKeys.REFRESH_TOKEN] = refreshToken
//        }
//    }
//
//    suspend fun getAccessToken(): String? =
//        dataStore.data.first()[PreferencesKeys.ACCESS_TOKEN]
//
//    suspend fun getRefreshToken(): String? =
//        dataStore.data.first()[PreferencesKeys.REFRESH_TOKEN]

    suspend fun setDbxCredential(dbxCredential: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DBX_CREDENTIAL] = dbxCredential
        }
    }

    suspend fun getDbxCredential(): String? =
        dataStore.data.first()[PreferencesKeys.DBX_CREDENTIAL]

    suspend fun setDbxCachingTrackingFileHash(dbxCachingTrackFileHash: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DBX_CACHING_TRACK_FILE_HASH] = dbxCachingTrackFileHash
        }
    }

    suspend fun getDbxCachingTrackingFileHash(): String? =
        dataStore.data.first()[PreferencesKeys.DBX_CACHING_TRACK_FILE_HASH]

    // TODO probably remove
//    suspend fun clearData() {
//        dataStore.edit { preferences -> preferences.clear() }
//    }

    private object PreferencesKeys {
//        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
//        val REFRESH_TOKEN = stringPreferencesKey("refreshToken")
        val DBX_CREDENTIAL = stringPreferencesKey("dbxCredential")
        val DBX_CACHING_TRACK_FILE_HASH = stringPreferencesKey("dbxCachingTrackFileHash")
    }
}