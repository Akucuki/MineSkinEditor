package com.example.mineskineditorlibgdx.persistence.dropboxCaching.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.model.FileInfo
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.model.FolderInfo

@Database(entities = [FolderInfo::class, FileInfo::class], version = 1)
abstract class DropboxCachingDatabase : RoomDatabase() {
    abstract fun cachingInfoDao(): CachingInfoDao
}