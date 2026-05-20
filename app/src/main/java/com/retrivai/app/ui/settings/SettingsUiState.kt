package com.retrivai.app.ui.settings

import com.retrivai.app.domain.model.GemmaModel
import com.retrivai.app.domain.model.GridDensity
import com.retrivai.app.domain.model.IndexingMode
import com.retrivai.app.domain.model.IndexingProgress

data class SettingsUiState(
    val indexingProgress: IndexingProgress = IndexingProgress.idle(),
    val indexingMode: IndexingMode = IndexingMode.BATTERY_AND_WIFI,
    // Epic 7 – model management
    val selectedModel: GemmaModel = GemmaModel.GEMMA_1B,
    val isModelDownloaded: Boolean = false,
    val detectedDefaultModel: GemmaModel = GemmaModel.GEMMA_1B,
    // Epic 8 – display / feature settings
    val gridDensity: GridDensity = GridDensity.MEDIUM,
    val autoAlbumEnabled: Boolean = true,
    val faceRecognitionEnabled: Boolean = true,
    val notifyIndexingComplete: Boolean = true,
    val notifyNewFaces: Boolean = true,
    val notifyNewAlbums: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)