package com.retrivai.app.ui.gallery

import com.retrivai.app.domain.model.Photo

data class GalleryUiState(
    val photos: List<Photo> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val hasPermission: Boolean = false,
    val permissionDeniedPermanently: Boolean = false
)
