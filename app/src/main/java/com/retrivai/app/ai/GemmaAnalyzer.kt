package com.retrivai.app.ai

import android.net.Uri
import com.retrivai.app.domain.model.PhotoTag

interface GemmaAnalyzer {
    suspend fun analyzePhoto(photoUri: Uri): PhotoTag
    suspend fun isModelLoaded(): Boolean
    suspend fun loadModel()
    suspend fun unloadModel()
}