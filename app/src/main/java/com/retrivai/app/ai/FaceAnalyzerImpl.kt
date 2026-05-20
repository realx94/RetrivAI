package com.retrivai.app.ai

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.retrivai.app.domain.model.DetectedFace
import com.retrivai.app.domain.model.FaceLandmark
import com.retrivai.app.domain.model.Point
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark as MLFaceLandmark
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaceAnalyzerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : FaceAnalyzer {

    private var isModelReady = false
    private val modelMutex = Mutex()
    private var faceDetector: FaceDetector? = null

    private fun getFaceDetector(): FaceDetector {
        if (faceDetector == null) {
            val options = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setMinFaceSize(0.15f)
                .build()

            faceDetector = FaceDetection.getClient(options)
        }
        return faceDetector!!
    }

    override suspend fun loadModel() = withContext(Dispatchers.IO) {
        modelMutex.withLock {
            if (isModelReady) return@withLock
            getFaceDetector()
            isModelReady = true
        }
    }

    override suspend fun isModelLoaded(): Boolean = isModelReady

    override suspend fun unloadModel() = withContext(Dispatchers.IO) {
        modelMutex.withLock {
            faceDetector?.close()
            faceDetector = null
            isModelReady = false
        }
    }

    override suspend fun detectFaces(photoUri: Uri): List<DetectedFace> = withContext(Dispatchers.IO) {
        var bitmap: Bitmap? = null
        try {
            bitmap = context.contentResolver.openInputStream(photoUri)?.use {
                BitmapFactory.decodeStream(it)
            } ?: return@withContext emptyList()

            val image = InputImage.fromBitmap(bitmap, 0)
            val faces = getFaceDetector().process(image).await()

            faces.mapIndexed { index, face ->
                val photoId = extractPhotoId(photoUri)
                DetectedFace(
                    faceId = generateFaceId(photoUri, index),
                    photoId = photoId,
                    boundingBox = face.boundingBox,
                    embedding = extractEmbedding(face),
                    landmarks = extractLandmarks(face),
                    confidence = 1.0f  // ML Kit doesn't provide confidence, use 1.0 as default
                )
            }
        } catch (e: Exception) {
            emptyList()
        } finally {
            bitmap?.recycle()
        }
    }

    private fun extractPhotoId(photoUri: Uri): Long {
        // Use the last path segment which is the MediaStore ID - more reliable than hashCode
        val pathSegment = photoUri.lastPathSegment
        return pathSegment?.toLongOrNull() ?: photoUri.hashCode().toLong().let {
            if (it < 0) -it else it
        }
    }

    private fun extractEmbedding(face: Face): List<Float> {
        val embedding = mutableListOf<Float>()

        // Head angles (yaw, roll, pitch) - normalized
        embedding.add(face.headEulerAngleY / 45f)
        embedding.add(face.headEulerAngleZ / 45f)

        // Face bounds ratio
        val box = face.boundingBox
        embedding.add(box.width().toFloat() / 1000f)
        embedding.add(box.height().toFloat() / 1000f)
        embedding.add((box.centerX().toFloat() / 1000f))
        embedding.add((box.centerY().toFloat() / 1000f))

        // Eye distance (normalized)
        val leftEye = face.getLandmark(MLFaceLandmark.LEFT_EYE)?.position
        val rightEye = face.getLandmark(MLFaceLandmark.RIGHT_EYE)?.position
        if (leftEye != null && rightEye != null) {
            val dx = (rightEye.x - leftEye.x).toDouble()
            val dy = (rightEye.y - leftEye.y).toDouble()
            val eyeDistance = kotlin.math.sqrt(dx * dx + dy * dy)
            embedding.add((eyeDistance / 500f).toFloat())
        } else {
            embedding.add(0f)
        }

        // Pad to consistent size (128 dimensions for pseudo-embedding)
        while (embedding.size < 128) {
            embedding.add(0f)
        }

        return embedding.take(128)
    }

    private fun extractLandmarks(face: Face): Map<FaceLandmark, List<Point>> {
        val landmarks = mutableMapOf<FaceLandmark, List<Point>>()

        face.getLandmark(MLFaceLandmark.LEFT_EYE)?.let { landmark ->
            landmarks[FaceLandmark.LEFT_EYE] = listOf(
                Point(landmark.position.x, landmark.position.y)
            )
        }

        face.getLandmark(MLFaceLandmark.RIGHT_EYE)?.let { landmark ->
            landmarks[FaceLandmark.RIGHT_EYE] = listOf(
                Point(landmark.position.x, landmark.position.y)
            )
        }

        face.getLandmark(MLFaceLandmark.NOSE)?.let { landmark ->
            landmarks[FaceLandmark.NOSE] = listOf(
                Point(landmark.position.x, landmark.position.y)
            )
        }

        face.getLandmark(MLFaceLandmark.LEFT_MOUTH)?.let { landmark ->
            landmarks[FaceLandmark.LEFT_MOUTH] = listOf(
                Point(landmark.position.x, landmark.position.y)
            )
        }

        face.getLandmark(MLFaceLandmark.RIGHT_MOUTH)?.let { landmark ->
            landmarks[FaceLandmark.RIGHT_MOUTH] = listOf(
                Point(landmark.position.x, landmark.position.y)
            )
        }

        face.getLandmark(MLFaceLandmark.BOTTOM_MOUTH)?.let { landmark ->
            landmarks[FaceLandmark.BOTTOM_MOUTH] = listOf(
                Point(landmark.position.x, landmark.position.y)
            )
        }

        return landmarks
    }

    private fun generateFaceId(photoUri: Uri, faceIndex: Int): Long {
        val uriHash = photoUri.hashCode().toLong()
        return (uriHash xor faceIndex.toLong()) and 0xFFFFFFFFL
    }
}