package com.example.mineskineditorlibgdx.persistence.dropboxCaching

import android.content.Context
import android.net.Uri
import android.util.Log
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.FolderMetadata
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.persistence.DataStoreHandler
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.db.CachingInfoDao
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.model.FileInfo
import com.example.mineskineditorlibgdx.persistence.dropboxCaching.model.FolderInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("unused")
@Singleton
class DropboxCachingProxy @Inject constructor(
    // TODO probably get rid of it
    @ApplicationContext
    private val context: Context,
    private val dbxClientV2: DbxClientV2,
    private val dataStoreHandler: DataStoreHandler,
    private val cachingInfoDao: CachingInfoDao
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var trackingFileInspectionJob: Job? = null
    private var indexingJob: Job? = null
    private val cacheDir = File(context.filesDir, context.getString(R.string.cache_dir_name))

    //TODO indexing job

    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        trackingFileInspectionJob = scope.launch {
            if (isTrackingFileChanged()) {
                Log.d("vitalik", "Tracking file changed, deleting cache dir")
                cachingInfoDao.deleteAllFoldersInfo()
                if (!cacheDir.deleteRecursively()) throw Exception("Failed to delete cache dir")
            }
        }
    }

    suspend fun indexRemoteFolder(folderPath: String) {
        require(isFolderOnRemote(folderPath)) {
            throw IllegalArgumentException(
                "The path provided \"$folderPath\" for indexing is not a folder!"
            )
        }
        var hasMore: Boolean
        var cursor: String? = null
        val folderInfo = FolderInfo(folderPath)
        cachingInfoDao.insertFolderInfo(folderInfo)
        do {
            val result = if (cursor == null) {
                dbxClientV2.files().listFolder(folderPath)
            } else {
                dbxClientV2.files().listFolderContinue(cursor)
            }
            hasMore = result.hasMore
            cursor = result.cursor
            result.entries.map { entry ->
                if (!isFolderOnRemote(entry.pathLower)) {
                    cachingInfoDao.insertFileInfo(
                        FileInfo(
                            path = entry.pathLower,
                            directoryPath = folderInfo.path
                        )
                    )
                }
            }
        } while (hasMore)
        cachingInfoDao.updateFolderInfo(folderInfo.copy(isIndexingFinished = true))
    }

//    suspend fun getFilesDetailsChunk(
//        folderPath: String,
//        cursor: String? = null
//    )
//    suspend fun getFilesDetailsChunk(
//        path: String,
//        cursor: String? = null,
//        // TODO probably redundant
//        limit: Long = 10
//    ): FilesDetailsChunk = withContext(Dispatchers.IO) {
//        val file = File(cacheDir, path)
//        if (file.exists() && file.isDirectory) {
//            return@withContext FilesDetailsChunk(
//                paths = file.list()?.toList() ?: emptyList(),
//                cursor = null,
//                hasMore = false
//            )
//        }
//        val result = if (cursor == null) {
//            dbxClientV2.files().listFolderBuilder(path).withLimit(limit).start()
//        } else {
//            dbxClientV2.files().listFolderContinue(cursor)
//        }
//        val filePaths = result.entries.map { it.pathLower }.filter { !isFolderOnRemote(it) }
//        val hasMore = result.hasMore
//        return@withContext FilesDetailsChunk(
//            paths = filePaths,
//            limit = limit,
//            cursor = result.cursor,
//            hasMore = hasMore
//        )
//    }

    suspend fun getFile(filePath: String): Uri? = withContext(Dispatchers.IO) {
        trackingFileInspectionJob?.join()
        val cachedFile = File(cacheDir, filePath)
        var cachedFileInfo = cachingInfoDao.getFileInfo(filePath)
        if (cachedFile.exists() && cachedFileInfo != null && cachedFileInfo.isFullyDownloaded) {
            Log.d("vitalik", "Cached file exists, returning it")
            return@withContext Uri.fromFile(cachedFile)
        }
        cachedFile.parentFile?.mkdirs()
        cachedFile.createNewFile()
        if (cachedFileInfo == null) {
            cachedFileInfo = FileInfo(
                path = cachedFile.path,
                directoryPath = cachedFile.parent ?: ""
            )
            cachingInfoDao.insertFileInfo(cachedFileInfo)
        }
        FileOutputStream(cachedFile).use {
            Log.d("vitalik", "Cached file is non-existent, downloading it")
            try {
                val downloader = dbxClientV2.files().download(filePath)
                val metadata = downloader.result
                Log.d("vitalik", "Remote file metadata: $metadata")
                downloader.download(it)
            } catch (e: Exception) {
                Log.d("vitalik", "Failed to download file from remote, returning null")
                cachedFile.delete()
                return@withContext null
            }
            it.fd.sync()
        }
        cachingInfoDao.updateFileInfo(cachedFileInfo.copy(isFullyDownloaded = true))
        return@withContext Uri.fromFile(cachedFile)
    }
    // TODO error handling
//    suspend fun getFile(filePath: String): Uri = withContext(Dispatchers.IO) {
//        trackingFileInspectionJob?.join()
//        val cachedFile = File(cacheDir, filePath)
//        if (cachedFile.exists()) {
//            Log.d("vitalik", "Cached file exists, returning it")
//            return@withContext Uri.fromFile(cachedFile)
//        }
//        cachedFile.parentFile?.mkdirs()
//        cachedFile.createNewFile()
//        FileOutputStream(cachedFile).use {
//            Log.d("vitalik", "Cached file is non-existent, downloading it")
//            val downloader = dbxClientV2.files().download(filePath)
//            val metadata = downloader.result
//            Log.d("vitalik", "Remote file metadata: $metadata")
//            downloader.download(it)
//            it.fd.sync()
//        }
//        return@withContext Uri.fromFile(cachedFile)
//    }

    fun onDestroy() {
        job.cancel()
    }

    private fun isFolderOnRemote(path: String): Boolean {
        val metadata = dbxClientV2.files().getMetadata(path)
        return metadata is FolderMetadata
    }

    private suspend fun isTrackingFileChanged(): Boolean {
        val cachingTrackingFilePath = context.getString(R.string.dropbox_caching_track_file_path)
        val remoteTrackingFileMetadata = dbxClientV2.files().getMetadata(cachingTrackingFilePath)
        val remoteTrackingFileFileMetadata = (remoteTrackingFileMetadata as? FileMetadata)
            ?: throw Exception("The specified remote tracking file is not a file")
        val remoteTrackingFileHash = remoteTrackingFileFileMetadata.contentHash
        val localTrackingFileHash = dataStoreHandler.getDbxCachingTrackingFileHash()
        dataStoreHandler.setDbxCachingTrackingFileHash(remoteTrackingFileHash)
        return remoteTrackingFileHash != localTrackingFileHash
    }
}