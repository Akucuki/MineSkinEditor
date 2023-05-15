package com.example.mineskineditorlibgdx.persistence

import android.content.Context
import android.net.Uri
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import com.dropbox.core.v2.files.FolderMetadata
import com.example.mineskineditorlibgdx.R
import com.example.mineskineditorlibgdx.model.FilesDetailsChunk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DropboxCachingProxy @Inject constructor(
    // TODO probably get rid of it
    private val context: Context,
    private val dbxClientV2: DbxClientV2,
    private val dataStoreHandler: DataStoreHandler
) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val cacheDir = File(context.cacheDir, context.getString(R.string.cache_dir_name))

    init {
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        scope.launch {
            if (isTrackFileChanged()) {
                if (!cacheDir.deleteRecursively()) throw Exception("Failed to delete cache dir")
            }
        }
    }

    suspend fun getFilesDetailsChunk(
        path: String,
        cursor: String? = null,
        // TODO probably redundant
        limit: Long = 10
    ): FilesDetailsChunk {
        val file = File(cacheDir, path)
        if (file.exists() && file.isDirectory) {
            return FilesDetailsChunk(
                paths = file.list()?.toList() ?: emptyList(),
                cursor = null,
                hasMore = false
            )
        }
        return withContext(Dispatchers.IO) {
            val result = if (cursor == null) {
                dbxClientV2.files().listFolderBuilder(path).withLimit(limit).start()
            } else {
                dbxClientV2.files().listFolderContinue(cursor)
            }
            val filePaths = result.entries.map { it.pathLower }.filter { !isFolderOnRemote(it) }
            val hasMore = result.hasMore
            FilesDetailsChunk(
                paths = filePaths,
                limit = limit,
                cursor = result.cursor,
                hasMore = hasMore
            )
        }
    }

    // TODO error handling
    suspend fun getFile(filePath: String): Uri {
        val cachedFile = File(cacheDir, filePath)
        if (cachedFile.exists()) return Uri.fromFile(cachedFile)
        return withContext(Dispatchers.IO) {
            cachedFile.createNewFile()
            FileOutputStream(cachedFile).use {
                dbxClientV2.files().download(filePath).download(it)
            }
            Uri.fromFile(cachedFile)
        }
    }

    fun onDestroy() {
        job.cancel()
    }

    private fun isFolderOnRemote(path: String): Boolean {
        val metadata = dbxClientV2.files().getMetadata(path)
        return metadata is FolderMetadata
    }

    private suspend fun isTrackFileChanged(): Boolean {
        val cachingTrackFilePath = context.getString(R.string.dropbox_caching_track_file_path)
        val remoteTrackFileMetadata = dbxClientV2.files().getMetadata(cachingTrackFilePath)
        val remoteTrackFileFileMetadata = (remoteTrackFileMetadata as? FileMetadata)
            ?: throw Exception("Remote track file is not a file")
        val remoteTrackFileHash = remoteTrackFileFileMetadata.contentHash
        val localTrackFileHash = dataStoreHandler.getDbxCachingTrackFileHash()
        return remoteTrackFileHash != localTrackFileHash
    }
}