package com.retrivai.app.data.repository

import com.retrivai.app.data.local.database.VideoTagEntity
import com.retrivai.app.data.local.database.dao.VideoTagDao
import com.retrivai.app.domain.model.VideoTag
import com.retrivai.app.domain.repository.VideoTagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject

class VideoTagRepositoryImpl @Inject constructor(
    private val videoTagDao: VideoTagDao
) : VideoTagRepository {

    override fun getVideoTag(videoId: Long): Flow<VideoTag?> {
        return videoTagDao.getVideoTag(videoId).map { entity ->
            entity?.toDomainModel()
        }
    }

    override fun getAllVideoTags(): Flow<List<VideoTag>> {
        return videoTagDao.getAllVideoTags().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun saveVideoTag(videoTag: VideoTag) {
        videoTagDao.insertVideoTag(videoTag.toEntity())
    }

    override suspend fun saveVideoTags(videoTags: List<VideoTag>) {
        videoTagDao.insertVideoTags(videoTags.map { it.toEntity() })
    }

    override suspend fun deleteVideoTag(videoId: Long) {
        videoTagDao.deleteVideoTag(videoId)
    }

    override suspend fun deleteAllVideoTags() {
        videoTagDao.deleteAllVideoTags()
    }

    private fun VideoTagEntity.toDomainModel(): VideoTag {
        return VideoTag(
            videoId = videoId,
            tags = parseJsonArray(tags),
            scenes = parseJsonArray(scenes),
            objects = parseJsonArray(objects),
            locations = parseJsonArray(locations),
            events = parseJsonArray(events),
            durationMs = durationMs,
            frameCount = frameCount,
            analyzedAt = analyzedAt
        )
    }

    private fun VideoTag.toEntity(): VideoTagEntity {
        return VideoTagEntity(
            videoId = videoId,
            tags = toJsonArray(tags),
            scenes = toJsonArray(scenes),
            objects = toJsonArray(objects),
            locations = toJsonArray(locations),
            events = toJsonArray(events),
            durationMs = durationMs,
            frameCount = frameCount,
            analyzedAt = analyzedAt
        )
    }

    private fun parseJsonArray(json: String): List<String> {
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { array.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun toJsonArray(list: List<String>): String {
        return JSONArray(list).toString()
    }
}