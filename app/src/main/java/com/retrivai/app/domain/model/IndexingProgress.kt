package com.retrivai.app.domain.model

sealed class IndexingProgressState {
    data object Idle : IndexingProgressState()
    data object Indexing : IndexingProgressState()
    data object PausedLowBattery : IndexingProgressState()
    data object Completed : IndexingProgressState()
}

data class IndexingProgress(
    val state: IndexingProgressState,
    val indexedPhotos: Int,
    val totalPhotos: Int,
    val percentage: Int
) {
    companion object {
        fun idle() = IndexingProgress(Idle, 0, 0, 0)

        fun fromIndexingState(indexingState: IndexingState): IndexingProgress {
            val percentage = if (indexingState.totalPhotos > 0) {
                ((indexingState.indexedPhotos.toFloat() / indexingState.totalPhotos) * 100).toInt()
                    .coerceIn(0, 100)
            } else {
                0
            }

            return when {
                indexingState.indexedPhotos >= indexingState.totalPhotos && indexingState.totalPhotos > 0 -> {
                    IndexingProgress(Completed, indexingState.indexedPhotos, indexingState.totalPhotos, 100)
                }
                !indexingState.isRunning && indexingState.indexedPhotos > 0 -> {
                    IndexingProgress(PausedLowBattery, indexingState.indexedPhotos, indexingState.totalPhotos, percentage)
                }
                indexingState.isRunning -> {
                    IndexingProgress(Indexing, indexingState.indexedPhotos, indexingState.totalPhotos, percentage)
                }
                else -> IndexingProgress(Idle, 0, 0, 0)
            }
        }
    }
}