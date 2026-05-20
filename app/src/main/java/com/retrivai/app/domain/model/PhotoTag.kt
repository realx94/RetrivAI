package com.retrivai.app.domain.model

data class PhotoTag(
    val photoId: Long,
    val tags: List<String>,
    val people: List<String> = emptyList(),
    val objects: List<String> = emptyList(),
    val locations: List<String> = emptyList(),
    val events: List<String> = emptyList(),
    val analyzedAt: Long = System.currentTimeMillis()
)