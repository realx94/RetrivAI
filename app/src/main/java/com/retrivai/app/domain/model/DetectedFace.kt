package com.retrivai.app.domain.model

import android.graphics.Rect

data class DetectedFace(
    val faceId: Long,
    val photoId: Long,
    val boundingBox: Rect,
    val embedding: List<Float>,
    val landmarks: Map<FaceLandmark, List<Point>> = emptyMap(),
    val confidence: Float = 1.0f
)

enum class FaceLandmark {
    LEFT_EYE,
    RIGHT_EYE,
    NOSE,
    LEFT_MOUTH,
    RIGHT_MOUTH,
    BOTTOM_MOUTH
}

data class Point(
    val x: Float,
    val y: Float
)
