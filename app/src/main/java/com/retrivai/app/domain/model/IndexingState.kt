package com.retrivai.app.domain.model

data class IndexingState(
    val lastIndexedPhotoId: Long = 0,
    val totalPhotos: Int = 0,
    val indexedPhotos: Int = 0,
    val isRunning: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    val progress: Float get() = if (totalPhotos > 0) indexedPhotos.toFloat() / totalPhotos else 0f
}