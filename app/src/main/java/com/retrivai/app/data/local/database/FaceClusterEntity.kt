package com.retrivai.app.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "face_clusters")
data class FaceClusterEntity(
    @PrimaryKey
    @ColumnInfo(name = "cluster_id")
    val clusterId: Long,

    @ColumnInfo(name = "face_count")
    val faceCount: Int,

    @ColumnInfo(name = "embedding_json")
    val embeddingJson: String,

    @ColumnInfo(name = "sample_photo_id")
    val samplePhotoId: Long,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
