package com.retrivai.app.data.repository

import com.retrivai.app.data.local.database.IndexingStateEntity
import com.retrivai.app.data.local.database.dao.IndexingStateDao
import com.retrivai.app.domain.model.IndexingState
import com.retrivai.app.domain.repository.IndexingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IndexingRepositoryImpl @Inject constructor(
    private val indexingStateDao: IndexingStateDao
) : IndexingRepository {

    override fun getIndexingState(): Flow<IndexingState?> {
        return indexingStateDao.getIndexingState().map { entity ->
            entity?.toDomainModel()
        }
    }

    override suspend fun getIndexingStateSync(): IndexingState? {
        return indexingStateDao.getIndexingStateSync()?.toDomainModel()
    }

    override suspend fun saveIndexingState(state: IndexingState) {
        indexingStateDao.updateIndexingState(state.toEntity())
    }

    override suspend fun updateProgress(indexedPhotos: Int, lastPhotoId: Long) {
        indexingStateDao.updateProgress(
            indexedPhotos = indexedPhotos,
            lastPhotoId = lastPhotoId,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun setRunning(running: Boolean) {
        indexingStateDao.updateRunningStatus(
            isRunning = running,
            lastUpdated = System.currentTimeMillis()
        )
    }

    private fun IndexingStateEntity.toDomainModel(): IndexingState {
        return IndexingState(
            lastIndexedPhotoId = lastIndexedPhotoId,
            totalPhotos = totalPhotos,
            indexedPhotos = indexedPhotos,
            isRunning = isRunning,
            lastUpdated = lastUpdated
        )
    }

    private fun IndexingState.toEntity(): IndexingStateEntity {
        return IndexingStateEntity(
            id = 1,
            lastIndexedPhotoId = lastIndexedPhotoId,
            totalPhotos = totalPhotos,
            indexedPhotos = indexedPhotos,
            isRunning = isRunning,
            lastUpdated = lastUpdated
        )
    }
}