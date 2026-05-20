package com.retrivai.app.domain.model

data class VideoTag(
    val videoId: Long,
    val tags: List<String>,
    val scenes: List<String> = emptyList(),
    val objects: List<String> = emptyList(),
    val locations: List<String> = emptyList(),
    val events: List<String> = emptyList(),
    val durationMs: Long = 0,
    val frameCount: Int = 0,
    val analyzedAt: Long = System.currentTimeMillis()
)