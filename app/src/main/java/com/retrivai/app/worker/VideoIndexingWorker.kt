package com.retrivai.app.worker

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Process
import android.provider.MediaStore
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.retrivai.app.ai.GemmaAnalyzer
import com.retrivai.app.data.local.database.IndexingStateEntity
import com.retrivai.app.data.local.database.dao.IndexingStateDao
import com.retrivai.app.domain.model.VideoTag
import com.retrivai.app.domain.repository.VideoTagRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltWorker
class VideoIndexingWorker @Inject constructor(
    @ApplicationContext private val context: android.content.Context,
    workerParams: WorkerParameters,
    private val indexingStateDao: IndexingStateDao,
    private val gemmaAnalyzer: GemmaAnalyzer,
    private val videoTagRepository: VideoTagRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND + 10)

            if (!gemmaAnalyzer.isModelLoaded()) {
                gemmaAnalyzer.loadModel()
            }

            val state = indexingStateDao.getIndexingStateSync()
                ?: indexingStateDao.getIndexingStateSync()
                ?: return@withContext Result.retry()

            val lastIndexedId = state.lastIndexedPhotoId
            val videosToProcess = getUnindexedVideos(lastIndexedId)

            if (videosToProcess.isEmpty()) {
                indexingStateDao.updateRunningStatus(false, System.currentTimeMillis())
                return@withContext Result.success()
            }

            var processedCount = 0
            var lastProcessedId = lastIndexedId

            for (video in videosToProcess) {
                val videoTag = analyzeVideo(video.id, video.uri)

                videoTagRepository.saveVideoTag(videoTag)

                processedCount++
                lastProcessedId = video.id

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
            if (runAttemptCount < MAX_RETRIES) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private suspend fun analyzeVideo(videoId: Long, videoUri: Uri): VideoTag = withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        val tempFiles = mutableListOf<File>()

        try {
            retriever.setDataSource(context, videoUri)

            val durationMs = retriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION
            )?.toLongOrNull() ?: 0L

            val frameCount = calculateFrameCount(durationMs)
            val allTags = mutableListOf<String>()
            val allScenes = mutableListOf<String>()
            val allObjects = mutableListOf<String>()
            val allLocations = mutableListOf<String>()
            val allEvents = mutableListOf<String>()

            for (i in 0 until frameCount) {
                val timeUs = (i * FRAME_INTERVAL_MS * 1000)
                val frame = retriever.getFrameAtTime(timeUs, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)

                if (frame != null) {
                    val frameFile = saveFrameToTempFile(frame, i)
                    tempFiles.add(frameFile)
                    val frameUri = Uri.fromFile(frameFile)

                    val frameTags = gemmaAnalyzer.analyzePhoto(frameUri)
                    if (frameTags != null) {
                        allTags.addAll(frameTags.tags)
                        allScenes.addAll(frameTags.scenes)
                        allObjects.addAll(frameTags.objects)
                        allLocations.addAll(frameTags.locations)
                        allEvents.addAll(frameTags.events)
                    }
                }
            }

            VideoTag(
                videoId = videoId,
                tags = allTags.distinct(),
                scenes = allScenes.distinct(),
                objects = allObjects.distinct(),
                locations = allLocations.distinct(),
                events = allEvents.distinct(),
                durationMs = durationMs,
                frameCount = frameCount,
                analyzedAt = System.currentTimeMillis()
            )
        } finally {
            retriever.release()
            cleanupTempFiles(tempFiles)
        }
    }

    private fun calculateFrameCount(durationMs: Long): Int {
        val count = ((durationMs + FRAME_INTERVAL_MS - 1) / FRAME_INTERVAL_MS).toInt()
        return count.coerceIn(1, MAX_FRAMES_PER_VIDEO)
    }

    private fun saveFrameToTempFile(bitmap: Bitmap, index: Int): File {
        val file = File(context.cacheDir, "frame_${System.currentTimeMillis()}_$index.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file
    }

    private fun cleanupTempFiles(files: List<File>) {
        for (file in files) {
            try {
                file.delete()
            } catch (e: Exception) {
                // Ignore cleanup errors
            }
        }
    }

    private suspend fun getUnindexedVideos(lastIndexedId: Long): List<VideoInfo> = withContext(Dispatchers.IO) {
        val videos = mutableListOf<VideoInfo>()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DURATION
        )

        val selection = if (lastIndexedId > 0) {
            "${MediaStore.Video.Media._ID} > ?"
        } else {
            null
        }

        val selectionArgs = if (lastIndexedId > 0) {
            arrayOf(lastIndexedId.toString())
        } else {
            null
        }

        val sortOrder = "${MediaStore.Video.Media._ID} ASC"

        try {
            context.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID)
                if (idColumn < 0) {
                    return@withContext emptyList()
                }
                var count = 0

                while (cursor.moveToNext() && count < MAX_VIDEOS_PER_BATCH) {
                    val id = cursor.getLong(idColumn)
                    videos.add(VideoInfo(
                        id = id,
                        uri = Uri.withAppendedPath(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            id.toString()
                        )
                    ))
                    count++
                }
            }
        } catch (e: Exception) {
            // Return empty on error, worker will retry
        }

        videos
    }

    data class VideoInfo(val id: Long, val uri: Uri)

    companion object {
        const val WORK_TAG = "video_indexing"
        private const val BATCH_SIZE = 50
        private const val MAX_VIDEOS_PER_BATCH = 100
        private const val FRAME_INTERVAL_MS = 5000L
        private const val MAX_FRAMES_PER_VIDEO = 20
        private const val MAX_RETRIES = 3
    }
}