package com.retrivai.app.data.repository

import com.retrivai.app.data.local.database.PhotoTagEntity
import com.retrivai.app.data.local.database.dao.PhotoTagDao
import com.retrivai.app.domain.model.PhotoTag
import com.retrivai.app.domain.repository.PhotoTagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject

class PhotoTagRepositoryImpl @Inject constructor(
    private val photoTagDao: PhotoTagDao
) : PhotoTagRepository {

    override fun getPhotoTag(photoId: Long): Flow<PhotoTag?> {
        return photoTagDao.getPhotoTag(photoId).map { entity ->
            entity?.toDomainModel()
        }
    }

    override fun getAllPhotoTags(): Flow<List<PhotoTag>> {
        return photoTagDao.getAllPhotoTags().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun savePhotoTag(photoTag: PhotoTag) {
        photoTagDao.insertPhotoTag(photoTag.toEntity())
    }

    override suspend fun savePhotoTags(photoTags: List<PhotoTag>) {
        photoTagDao.insertPhotoTags(photoTags.map { it.toEntity() })
    }

    override suspend fun deletePhotoTag(photoId: Long) {
        photoTagDao.deletePhotoTag(photoId)
    }

    override suspend fun deleteAllPhotoTags() {
        photoTagDao.deleteAllPhotoTags()
    }

    private fun PhotoTagEntity.toDomainModel(): PhotoTag {
        return PhotoTag(
            photoId = photoId,
            tags = parseJsonArray(tags),
            people = parseJsonArray(people),
            objects = parseJsonArray(objects),
            locations = parseJsonArray(locations),
            events = parseJsonArray(events),
            analyzedAt = analyzedAt
        )
    }

    private fun PhotoTag.toEntity(): PhotoTagEntity {
        return PhotoTagEntity(
            photoId = photoId,
            tags = toJsonArray(tags),
            people = toJsonArray(people),
            objects = toJsonArray(objects),
            locations = toJsonArray(locations),
            events = toJsonArray(events),
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