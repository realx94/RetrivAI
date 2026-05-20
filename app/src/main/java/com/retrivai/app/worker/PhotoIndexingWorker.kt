package com.retrivai.app.worker

import android.os.Process
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.retrivai.app.ai.GemmaAnalyzer
import com.retrivai.app.data.local.database.IndexingStateEntity
import com.retrivai.app.data.local.database.dao.IndexingStateDao
import com.retrivai.app.domain.repository.PhotoTagRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
class PhotoIndexingWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters,
    private val indexingStateDao: IndexingStateDao,
    private val gemmaAnalyzer: GemmaAnalyzer,
    private val photoTagRepository: PhotoTagRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND + 10)

            // Load Gemma model if not already loaded
            if (!gemmaAnalyzer.isModelLoaded()) {
                gemmaAnalyzer.loadModel()
            }

            val currentState = indexingStateDao.getIndexingStateSync()

            if (currentState == null) {
                indexingStateDao.updateIndexingState(
                    IndexingStateEntity(
                        id = 1,
                        isRunning = true,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            } else {
                indexingStateDao.updateRunningStatus(true, System.currentTimeMillis())
            }

            val state = indexingStateDao.getIndexingStateSync() ?: return@withContext Result.success()
            val lastIndexedId = state.lastIndexedPhotoId

            // Get photos to process from MediaStore
            val photosToProcess = getUnindexedPhotos(lastIndexedId)
            var processedCount = 0
            var lastProcessedId = lastIndexedId

            for (photo in photosToProcess) {
                // Analyze photo with Gemma AI
                val photoTag = gemmaAnalyzer.analyzePhoto(photo.uri)

                // Save tags to database
                photoTagRepository.savePhotoTag(photoTag)

                processedCount++
                lastProcessedId = photo.id

                if (processedCount > 0 && processedCount % BATCH_SIZE == 0) {
                    val totalProcessed = state.indexedPhotos + processedCount
                    indexingStateDao.updateProgress(
                        indexedPhotos = totalProcessed,
                        lastPhotoId = lastProcessedId,
                        lastUpdated = System.currentTimeMillis()
                    )
                }
            }

            indexingStateDao.updateProgress(
                indexedPhotos = state.indexedPhotos + processedCount,
                lastPhotoId = lastProcessedId,
                lastUpdated = System.currentTimeMillis()
            )
            indexingStateDao.updateRunningStatus(false, System.currentTimeMillis())

            Result.success()
        } catch (e: Exception) {
            gemmaAnalyzer.unloadModel()
            indexingStateDao.updateRunningStatus(false, System.currentTimeMillis())
            Result.failure()
        }
    }

    private suspend fun getUnindexedPhotos(lastIndexedId: Long): List<PhotoInfo> = withContext(Dispatchers.IO) {
        val photos = mutableListOf<PhotoInfo>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN
        )

        val selection = if (lastIndexedId > 0) {
            "${MediaStore.Images.Media._ID} > ?"
        } else {
            null
        }

        val selectionArgs = if (lastIndexedId > 0) {
            arrayOf(lastIndexedId.toString())
        } else {
            null
        }

        val sortOrder = "${MediaStore.Images.Media._ID} ASC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            var count = 0

            while (cursor.moveToNext() && count < MAX_PHOTOS_PER_BATCH) {
                val id = cursor.getLong(idColumn)
                photos.add(PhotoInfo(id = id, uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )))
                count++
            }
        }

        photos
    }

    data class PhotoInfo(val id: Long, val uri: Uri)

    companion object {
        const val WORK_TAG = "photo_indexing"
        private const val BATCH_SIZE = 50
        private const val MAX_PHOTOS_PER_BATCH = 500
    }
}