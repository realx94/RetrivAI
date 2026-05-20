package com.retrivai.app.domain.repository

import com.retrivai.app.domain.model.PhotoTag
import kotlinx.coroutines.flow.Flow

interface PhotoTagRepository {
    fun getPhotoTag(photoId: Long): Flow<PhotoTag?>
    fun getAllPhotoTags(): Flow<List<PhotoTag>>
    suspend fun savePhotoTag(photoTag: PhotoTag)
    suspend fun savePhotoTags(photoTags: List<PhotoTag>)
    suspend fun deletePhotoTag(photoId: Long)
    suspend fun deleteAllPhotoTags()
}