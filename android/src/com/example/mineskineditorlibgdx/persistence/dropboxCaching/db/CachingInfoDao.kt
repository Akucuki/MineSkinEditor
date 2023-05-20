package com.example.mineskineditorlibgdx.persistence.dropboxCaching.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.model.FolderInfo
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.model.FolderInfoWithFiles
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.model.FileInfo

@Dao
interface CachingInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolderInfo(folderInfo: FolderInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFileInfo(fileInfo: FileInfo)

    @Update
    suspend fun updateFolderInfo(folderInfo: FolderInfo)

    @Update
    suspend fun updateFileInfo(fileInfo: FileInfo)

    @Transaction
    @Query("SELECT * FROM file_info WHERE path = :path")
    suspend fun getFileInfo(path: String): FileInfo?

    @Transaction
    @Query("SELECT * FROM folder_info WHERE path = :path")
    suspend fun getFolderInfoWithFiles(path: String): FolderInfoWithFiles?

    @Query("DELETE FROM folder_info")
    suspend fun deleteAllFoldersInfo()
}