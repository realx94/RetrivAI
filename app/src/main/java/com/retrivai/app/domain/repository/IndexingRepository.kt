package com.retrivai.app.domain.repository

import com.retrivai.app.domain.model.IndexingState
import kotlinx.coroutines.flow.Flow

interface IndexingRepository {
    fun getIndexingState(): Flow<IndexingState?>
    suspend fun getIndexingStateSync(): IndexingState?
    suspend fun saveIndexingState(state: IndexingState)
    suspend fun updateProgress(indexedPhotos: Int, lastPhotoId: Long)
    suspend fun setRunning(running: Boolean)
}