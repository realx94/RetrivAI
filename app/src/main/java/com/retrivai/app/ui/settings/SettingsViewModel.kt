package com.retrivai.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retrivai.app.data.preferences.AppPreferences
import com.retrivai.app.data.preferences.IndexingPreferences
import com.retrivai.app.data.preferences.ModelPreferences
import com.retrivai.app.domain.model.GemmaModel
import com.retrivai.app.domain.model.GridDensity
import com.retrivai.app.domain.model.IndexingMode
import com.retrivai.app.domain.model.IndexingProgress
import com.retrivai.app.domain.repository.IndexingRepository
import com.retrivai.app.worker.IndexingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val indexingRepository: IndexingRepository,
    private val indexingPreferences: IndexingPreferences,
    private val indexingManager: IndexingManager,
    private val appPreferences: AppPreferences,
    private val modelPreferences: ModelPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        observeIndexingState()
    }

    private fun loadSettings() {
        _uiState.update {
            it.copy(
                indexingMode = indexingPreferences.getIndexingMode(),
                selectedModel = modelPreferences.getSelectedModel(),
                isModelDownloaded = modelPreferences.isModelDownloaded(modelPreferences.getSelectedModel()),
                detectedDefaultModel = modelPreferences.detectDefaultModel(),
                gridDensity = appPreferences.getGridDensity(),
                autoAlbumEnabled = appPreferences.isAutoAlbumEnabled(),
                faceRecognitionEnabled = appPreferences.isFaceRecognitionEnabled(),
                notifyIndexingComplete = appPreferences.isNotifyIndexingComplete(),
                notifyNewFaces = appPreferences.isNotifyNewFaces(),
                notifyNewAlbums = appPreferences.isNotifyNewAlbums()
            )
        }
    }

    fun onIndexingModeSelected(mode: IndexingMode) {
        indexingPreferences.setIndexingMode(mode)
        indexingManager.schedulePeriodicIndexing(mode)
        _uiState.update { it.copy(indexingMode = mode) }
    }

    fun onModelSelected(model: GemmaModel) {
        modelPreferences.setSelectedModel(model)
        _uiState.update {
            it.copy(
                selectedModel = model,
                isModelDownloaded = modelPreferences.isModelDownloaded(model)
            )
        }
    }

    fun onGridDensitySelected(density: GridDensity) {
        appPreferences.setGridDensity(density)
        _uiState.update { it.copy(gridDensity = density) }
    }

    fun onAutoAlbumToggled(enabled: Boolean) {
        appPreferences.setAutoAlbumEnabled(enabled)
        _uiState.update { it.copy(autoAlbumEnabled = enabled) }
    }

    fun onFaceRecognitionToggled(enabled: Boolean) {
        appPreferences.setFaceRecognitionEnabled(enabled)
        _uiState.update { it.copy(faceRecognitionEnabled = enabled) }
    }

    fun onNotifyIndexingCompleteToggled(enabled: Boolean) {
        appPreferences.setNotifyIndexingComplete(enabled)
        _uiState.update { it.copy(notifyIndexingComplete = enabled) }
    }

    fun onNotifyNewFacesToggled(enabled: Boolean) {
        appPreferences.setNotifyNewFaces(enabled)
        _uiState.update { it.copy(notifyNewFaces = enabled) }
    }

    fun onNotifyNewAlbumsToggled(enabled: Boolean) {
        appPreferences.setNotifyNewAlbums(enabled)
        _uiState.update { it.copy(notifyNewAlbums = enabled) }
    }

    private fun observeIndexingState() {
        viewModelScope.launch {
            indexingRepository.getIndexingState().collect { indexingState ->
                val progress = indexingState?.let {
                    IndexingProgress.fromIndexingState(it)
                } ?: IndexingProgress.idle()

                _uiState.update { it.copy(indexingProgress = progress, isLoading = false) }
            }
        }
    }
}