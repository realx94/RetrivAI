package com.retrivai.app.ui.photo

import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.retrivai.app.domain.model.Photo
import com.retrivai.app.domain.usecase.photo.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PhotoDetailUiState(
    val photos: List<Photo> = emptyList(),
    val currentIndex: Int = 0,
    val showControls: Boolean = true,
    val isLoading: Boolean = true
) {
    val currentPhoto: Photo?
        get() = photos.getOrNull(currentIndex)
}

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    application: android.app.Application
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(PhotoDetailUiState())
    val uiState: StateFlow<PhotoDetailUiState> = _uiState.asStateFlow()

    init {
        loadPhotos()
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getPhotosUseCase()
                .onSuccess { photos ->
                    _uiState.value = _uiState.value.copy(
                        photos = photos,
                        isLoading = false
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
        }
    }

    fun setInitialPhotoIndex(index: Int) {
        _uiState.value = _uiState.value.copy(currentIndex = index)
    }

    fun goToNextPhoto() {
        val current = _uiState.value.currentIndex
        if (current < _uiState.value.photos.size - 1) {
            _uiState.value = _uiState.value.copy(currentIndex = current + 1)
        }
    }

    fun goToPreviousPhoto() {
        val current = _uiState.value.currentIndex
        if (current > 0) {
            _uiState.value = _uiState.value.copy(currentIndex = current - 1)
        }
    }

    fun toggleControls() {
        _uiState.value = _uiState.value.copy(showControls = !_uiState.value.showControls)
    }

    fun hideControls() {
        _uiState.value = _uiState.value.copy(showControls = false)
    }
}