package com.retrivai.app.domain.repository

import com.retrivai.app.domain.model.VideoTag
import kotlinx.coroutines.flow.Flow

interface VideoTagRepository {
    fun getVideoTag(videoId: Long): Flow<VideoTag?>
    fun getAllVideoTags(): Flow<List<VideoTag>>
    suspend fun saveVideoTag(videoTag: VideoTag)
    suspend fun saveVideoTags(videoTags: List<VideoTag>)
    suspend fun deleteVideoTag(videoId: Long)
    suspend fun deleteAllVideoTags()
}