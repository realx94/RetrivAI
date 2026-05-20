package com.retrivai.app.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "indexing_state")
data class IndexingStateEntity(
    @PrimaryKey val id: Int = 1,
    val lastIndexedPhotoId: Long = 0,
    val totalPhotos: Int = 0,
    val indexedPhotos: Int = 0,
    val isRunning: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)