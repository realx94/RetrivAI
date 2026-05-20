package com.retrivai.app.ai

import android.graphics.Bitmap
import android.net.Uri
import com.retrivai.app.domain.model.DetectedFace

interface FaceAnalyzer {
    suspend fun detectFaces(photoUri: Uri): List<DetectedFace>
    suspend fun isModelLoaded(): Boolean
    suspend fun loadModel()
    suspend fun unloadModel()
}
