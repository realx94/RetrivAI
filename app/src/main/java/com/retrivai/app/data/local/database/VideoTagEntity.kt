package com.retrivai.app.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_tags")
data class VideoTagEntity(
    @PrimaryKey val videoId: Long,
    val tags: String,
    val scenes: String,
    val objects: String,
    val locations: String,
    val events: String,
    val durationMs: Long,
    val frameCount: Int,
    val analyzedAt: Long
)