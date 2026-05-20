package com.retrivai.app.domain.model

data class FaceCluster(
    val clusterId: Long,
    val faceCount: Int,
    val embedding: List<Float>,
    val samplePhotoId: Long,
    val name: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
