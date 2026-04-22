package com.retrivai.app.ui.gallery

import com.retrivai.app.domain.model.Photo
import com.retrivai.app.domain.model.Video

data class GalleryUiState(
    val photos: List<Photo> = emptyList(),
    val videos: List<Video> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val hasPermission: Boolean = false,
    val permissionDeniedPermanently: Boolean = false,
    val isSelectionMode: Boolean = false,
    val selectedPhotoIds: Set<Long> = emptySet(),
    val playingVideoId: Long? = null,
    val isVideoMuted: Boolean = false
) {
    val selectedCount: Int get() = selectedPhotoIds.size
}