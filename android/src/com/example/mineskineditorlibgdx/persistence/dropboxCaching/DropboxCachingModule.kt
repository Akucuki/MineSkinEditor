package com.example.mineskineditorlibgdx.persistence.dropboxCaching

import android.content.Context
import androidx.room.Room
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.db.CachingInfoDao
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.db.DropboxCachingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DropboxCachingModule {

    @Singleton
    @Provides
    fun provideDropboxCachingDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        DropboxCachingDatabase::class.java,
        "dropbox_caching_database"
    ).build()

    @Singleton
    @Provides
    fun provideDropboxCachingDao(dropboxCachingDatabase: DropboxCachingDatabase): CachingInfoDao {
        return dropboxCachingDatabase.cachingInfoDao()
    }
}