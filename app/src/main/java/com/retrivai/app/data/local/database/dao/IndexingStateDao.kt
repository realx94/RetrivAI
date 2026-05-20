package com.retrivai.app.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.retrivai.app.data.local.database.IndexingStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IndexingStateDao {

    @Query("SELECT * FROM indexing_state WHERE id = 1")
    fun getIndexingState(): Flow<IndexingStateEntity?>

    @Query("SELECT * FROM indexing_state WHERE id = 1")
    suspend fun getIndexingStateSync(): IndexingStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateIndexingState(state: IndexingStateEntity)

    @Query("UPDATE indexing_state SET indexedPhotos = :indexedPhotos, lastIndexedPhotoId = :lastPhotoId, lastUpdated = :lastUpdated WHERE id = 1")
    suspend fun updateProgress(indexedPhotos: Int, lastPhotoId: Long, lastUpdated: Long)

    @Query("UPDATE indexing_state SET isRunning = :isRunning, lastUpdated = :lastUpdated WHERE id = 1")
    suspend fun updateRunningStatus(isRunning: Boolean, lastUpdated: Long)
}