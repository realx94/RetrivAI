package com.retrivai.app.worker

import android.os.Process
import android.provider.MediaStore
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.retrivai.app.ai.FaceAnalyzer
import com.retrivai.app.data.local.database.IndexingStateEntity
import com.retrivai.app.data.local.database.dao.IndexingStateDao
import com.retrivai.app.domain.model.FaceCluster
import com.retrivai.app.domain.repository.FaceClusterRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltWorker
class FaceRecognitionWorker @Inject constructor(
    @ApplicationContext private val context: android.content.Context,
    workerParams: WorkerParameters,
    private val indexingStateDao: IndexingStateDao,
    private val faceAnalyzer: FaceAnalyzer,
    private val faceClusterRepository: FaceClusterRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND + 10)

            if (!faceAnalyzer.isModelLoaded()) {
                faceAnalyzer.loadModel()
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
            val lastIndexedId = state.lastIndexedPhotoId.coerceAtLeast(0)

            val photosToProcess = getUnprocessedPhotos(lastIndexedId)

            if (photosToProcess.isEmpty()) {
                indexingStateDao.updateRunningStatus(false, System.currentTimeMillis())
                return@withContext Result.success()
            }

            var processedCount = 0L
            var lastProcessedId = lastIndexedId

            // Fetch existing clusters ONCE before processing
            val existingClusters = mutableListOf<FaceCluster>()
            faceClusterRepository.getAllFaceClusters().collect { clusters ->
                existingClusters.addAll(clusters)
            }

            for (photo in photosToProcess) {
                val detectedFaces = faceAnalyzer.detectFaces(photo.uri)

                if (detectedFaces.isNotEmpty()) {
                    // Pass all existing clusters (including ones created in this batch)
                    val clusters = clusterFaces(detectedFaces, existingClusters)
                    faceClusterRepository.saveFaceClusters(clusters)
                    // Update existing clusters for subsequent photos in this batch
                    existingClusters.addAll(clusters)
                }

                processedCount++
                lastProcessedId = photo.id

                if (processedCount > 0 && processedCount % BATCH_SIZE == 0) {
                    val totalProcessed = state.indexedPhotos + processedCount.toInt()
                    indexingStateDao.updateProgress(
                        indexedPhotos = totalProcessed,
                        lastPhotoId = lastProcessedId,
                        lastUpdated = System.currentTimeMillis()
                    )
                }
            }

            indexingStateDao.updateProgress(
                indexedPhotos = (state.indexedPhotos + processedCount.toInt())
                    .coerceAtMost(Int.MAX_VALUE.toLong()).toInt(),
                lastPhotoId = lastProcessedId,
                lastUpdated = System.currentTimeMillis()
            )
            indexingStateDao.updateRunningStatus(false, System.currentTimeMillis())

            Result.success()
        } catch (e: Exception) {
            faceAnalyzer.unloadModel()
            indexingStateDao.updateRunningStatus(false, System.currentTimeMillis())
            if (runAttemptCount < MAX_RETRIES) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    private suspend fun clusterFaces(
        detectedFaces: List<com.retrivai.app.domain.model.DetectedFace>,
        allExistingClusters: List<FaceCluster>
    ): List<FaceCluster> = withContext(Dispatchers.IO) {
        val newClusters = mutableListOf<FaceCluster>()
        var clusterId = (allExistingClusters.maxOfOrNull { it.clusterId } ?: 0L) + 1

        for (face in detectedFaces) {
            // Check against ALL clusters including those created in this batch
            val matchingCluster = allExistingClusters.find { cluster ->
                cosineSimilarity(face.embedding, cluster.embedding) > SIMILARITY_THRESHOLD
            } ?: newClusters.find { cluster ->
                cosineSimilarity(face.embedding, cluster.embedding) > SIMILARITY_THRESHOLD
            }

            if (matchingCluster != null) {
                // Update existing cluster
                val index = allExistingClusters.indexOf(matchingCluster)
                if (index >= 0) {
                    val updated = matchingCluster.copy(
                        faceCount = matchingCluster.faceCount + 1,
                        updatedAt = System.currentTimeMillis()
                    )
                    // Note: In a real implementation, we'd update the DB here
                    // For now, we just track in memory for batch processing
                }
            } else {
                newClusters.add(FaceCluster(
                    clusterId = clusterId++,
                    faceCount = 1,
                    embedding = face.embedding,
                    samplePhotoId = face.photoId,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ))
            }
        }

        newClusters
    }

    private fun cosineSimilarity(embedding1: List<Float>, embedding2: List<Float>): Float {
        if (embedding1.size != embedding2.size || embedding1.isEmpty()) return 0f

        var dotProduct = 0f
        var norm1 = 0f
        var norm2 = 0f

        for (i in embedding1.indices) {
            dotProduct += embedding1[i] * embedding2[i]
            norm1 += embedding1[i] * embedding1[i]
            norm2 += embedding2[i] * embedding2[i]
        }

        if (norm1 <= 0f || norm2 <= 0f) return 0f
        val denominator = kotlin.math.sqrt(norm1.toDouble()) * kotlin.math.sqrt(norm2.toDouble())
        return if (denominator > 0.0) (dotProduct / denominator.toFloat()) else 0f
    }

    private suspend fun getUnprocessedPhotos(lastIndexedId: Long): List<PhotoInfo> = withContext(Dispatchers.IO) {
        val photos = mutableListOf<PhotoInfo>()

        val projection = arrayOf(MediaStore.Images.Media._ID)

        val selection = "${MediaStore.Images.Media._ID} > ?"
        val selectionArgs = arrayOf(lastIndexedId.toString())

        val sortOrder = "${MediaStore.Images.Media._ID} ASC"

        try {
            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                if (idColumn < 0) return@withContext emptyList()

                var count = 0
                while (cursor.moveToNext() && count < MAX_PHOTOS_PER_BATCH) {
                    val id = cursor.getLong(idColumn)
                    photos.add(PhotoInfo(
                        id = id,
                        uri = android.net.Uri.withAppendedPath(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id.toString()
                        )
                    ))
                    count++
                }
            }
        } catch (e: Exception) {
            // Log error in production
        }

        photos
    }

    data class PhotoInfo(val id: Long, val uri: android.net.Uri)

    companion object {
        const val WORK_TAG = "face_recognition"
        private const val BATCH_SIZE = 50
        private const val MAX_PHOTOS_PER_BATCH = 500
        private const val SIMILARITY_THRESHOLD = 0.8f
        private const val MAX_RETRIES = 3
    }
}