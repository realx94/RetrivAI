package com.retrivai.app.ui.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.retrivai.app.domain.usecase.photo.GetPhotosUseCase
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
    private val getPhotosUseCase: GetPhotosUseCase
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
        }
    }

    fun onPermissionGranted() {
        _uiState.update { it.copy(hasPermission = true, permissionDeniedPermanently = false) }
        loadPhotos()
    }

    fun onPermissionDenied(permanent: Boolean = false) {
        _uiState.update { it.copy(hasPermission = false, permissionDeniedPermanently = permanent) }
    }
}
