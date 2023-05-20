package com.example.mineskineditorlibgdx.persistence.dropboxCaching.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.model.FolderInfo
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.model.FileInfo

@Database(entities = [FolderInfo::class, FileInfo::class], version = 1)
abstract class DropboxCachingDatabase : RoomDatabase() {
    abstract fun cachingInfoDao(): CachingInfoDao
}