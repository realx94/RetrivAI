package com.retrivai.app.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_tags")
data class PhotoTagEntity(
    @PrimaryKey val photoId: Long,
    val tags: String,
    val people: String,
    val objects: String,
    val locations: String,
    val events: String,
    val analyzedAt: Long
)