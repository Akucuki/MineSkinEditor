package com.example.mineskineditorlibgdx.persistence.dropboxCaching.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "folder_info")
data class FolderInfo(
    @PrimaryKey
    val path: String,
    val isIndexingFinished: Boolean = false
)

@Entity(
    tableName = "file_info",
//    foreignKeys = [
//        ForeignKey(
//            entity = FolderInfo::class,
//            parentColumns = ["path"],
//            childColumns = ["directoryPath"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
)
data class FileInfo(
    @PrimaryKey
    val path: String,
    val directoryPath: String,
    val isFullyDownloaded: Boolean = false
)

data class FolderInfoWithFiles(
    @Embedded val folderInfo: FolderInfo,
    @Relation(
        parentColumn = "path",
        entityColumn = "directoryPath"
    )
    val files: List<FileInfo>
)