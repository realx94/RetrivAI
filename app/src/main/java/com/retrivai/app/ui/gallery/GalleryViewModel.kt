package com.retrivai.app.ui.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.retrivai.app.domain.model.Photo
import com.retrivai.app.domain.model.Video
import com.retrivai.app.domain.usecase.photo.GetPhotosUseCase
import com.retrivai.app.domain.usecase.video.GetVideosUseCase
import com.retrivai.app.util.PermissionUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    application: Application,
    private val getPhotosUseCase: GetPhotosUseCase,
    private val getVideosUseCase: GetVideosUseCase
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(GalleryUiState())
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    init {
        checkPermission()
    }

    fun checkPermission() {
        viewModelScope.launch {
            val hasPermission = PermissionUtils.hasAllMediaPermissions(getApplication())
            _uiState.update { it.copy(hasPermission = hasPermission, isLoading = false) }
            if (hasPermission) {
                loadPhotos()
                loadVideos()
            }
        }
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getPhotosUseCase()
                .onSuccess { photos ->
                    _uiState.update { it.copy(photos = photos, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    fun loadVideos() {
        viewModelScope.launch {
            getVideosUseCase()
                .onSuccess { videos ->
                    _uiState.update { it.copy(videos = videos) }
                }
                .onFailure { /* Videos are optional, don't show error */ }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            getPhotosUseCase()
                .onSuccess { photos ->
                    _uiState.update { it.copy(photos = photos, isRefreshing = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isRefreshing = false) }
                }
            getVideosUseCase()
                .onSuccess { videos ->
                    _uiState.update { it.copy(videos = videos) }
                }
        }
    }

    fun onPermissionGranted() {
        _uiState.update { it.copy(hasPermission = true, permissionDeniedPermanently = false) }
        loadPhotos()
        loadVideos()
    }

    fun onPermissionDenied(permanent: Boolean = false) {
        _uiState.update { it.copy(hasPermission = false, permissionDeniedPermanently = permanent) }
    }

    fun enterSelectionMode(photoId: Long) {
        _uiState.update {
            it.copy(
                isSelectionMode = true,
                selectedPhotoIds = setOf(photoId)
            )
        }
    }

    fun toggleSelection(photoId: Long) {
        _uiState.update { state ->
            val newSelection = if (state.selectedPhotoIds.contains(photoId)) {
                state.selectedPhotoIds - photoId
            } else {
                state.selectedPhotoIds + photoId
            }
            state.copy(
                selectedPhotoIds = newSelection,
                isSelectionMode = newSelection.isNotEmpty()
            )
        }
    }

    fun clearSelection() {
        _uiState.update {
            it.copy(
                isSelectionMode = false,
                selectedPhotoIds = emptySet()
            )
        }
    }

    fun getSelectedPhotos(): List<Photo> {
        return _uiState.value.photos.filter { it.id in _uiState.value.selectedPhotoIds }
    }

    fun playVideo(videoId: Long) {
        _uiState.update { it.copy(playingVideoId = videoId) }
    }

    fun stopVideo() {
        _uiState.update { it.copy(playingVideoId = null) }
    }

    fun toggleVideoMute() {
        _uiState.update { it.copy(isVideoMuted = !it.isVideoMuted) }
    }
}