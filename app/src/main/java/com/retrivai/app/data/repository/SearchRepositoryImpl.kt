package com.retrivai.app.data.repository

import com.retrivai.app.data.local.database.dao.FaceClusterDao
import com.retrivai.app.data.local.database.dao.PhotoTagDao
import com.retrivai.app.data.local.database.dao.VideoTagDao
import com.retrivai.app.domain.model.Photo
import com.retrivai.app.domain.model.SearchResult
import com.retrivai.app.domain.model.Video
import com.retrivai.app.domain.repository.PhotoRepository
import com.retrivai.app.domain.repository.SearchRepository
import com.retrivai.app.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val photoTagDao: PhotoTagDao,
    private val videoTagDao: VideoTagDao,
    private val faceClusterDao: FaceClusterDao,
    private val photoRepository: PhotoRepository,
    private val videoRepository: VideoRepository
) : SearchRepository {

    override fun search(query: String): Flow<List<SearchResult>> = flow {
        if (query.isBlank()) {
            emit(emptyList())
            return@flow
        }
        val normalizedQuery = query.lowercase().trim()
        val results = mutableListOf<SearchResult>()

        // Search photos via tags
        val photoResult = photoRepository.getPhotos()
        photoResult.onSuccess { photos ->
            val photoMap = photos.associateBy { it.id }
            val allTags = photoTagDao.getAllPhotoTagsSync()
            allTags.forEach { entity ->
                val allFields = listOf(entity.tags, entity.people, entity.objects, entity.locations, entity.events)
                val matchedTags = mutableListOf<String>()
                var score = 0f
                allFields.forEach { field ->
                    val parsed = parseJsonArray(field)
                    parsed.forEach { tag ->
                        if (tag.lowercase().contains(normalizedQuery)) {
                            matchedTags.add(tag)
                            score += 1f
                        }
                    }
                }
                if (matchedTags.isNotEmpty()) {
                    val photo = photoMap[entity.photoId]
                    if (photo != null) {
                        results.add(SearchResult.PhotoResult(photo, minOf(score / 5f, 1f), matchedTags))
                    }
                }
            }
        }

        // Search videos via tags
        val videoResult = videoRepository.getVideos()
        videoResult.onSuccess { videos ->
            val videoMap = videos.associateBy { it.id }
            val allVideoTags = videoTagDao.getAllVideoTagsSync()
            allVideoTags.forEach { entity ->
                val allFields = listOf(entity.tags, entity.people, entity.objects, entity.locations, entity.events)
                val matchedTags = mutableListOf<String>()
                var score = 0f
                allFields.forEach { field ->
                    val parsed = parseJsonArray(field)
                    parsed.forEach { tag ->
                        if (tag.lowercase().contains(normalizedQuery)) {
                            matchedTags.add(tag)
                            score += 1f
                        }
                    }
                }
                if (matchedTags.isNotEmpty()) {
                    val video = videoMap[entity.videoId]
                    if (video != null) {
                        results.add(SearchResult.VideoResult(video, minOf(score / 5f, 1f), matchedTags))
                    }
                }
            }
        }

        emit(results.sortedByDescending { it.relevanceScore })
    }

    override suspend fun searchByPersonName(name: String): List<SearchResult> {
        val results = mutableListOf<SearchResult>()
        val normalizedName = name.lowercase().trim()

        // Find face clusters with matching name
        val clusters = faceClusterDao.getAllFaceClustersSync()
        val matchingClusterIds = clusters
            .filter { it.name?.lowercase()?.contains(normalizedName) == true }
            .map { it.clusterId }

        if (matchingClusterIds.isEmpty()) return emptyList()

        // Find photos tagged with people matching those cluster names
        val allTags = photoTagDao.getAllPhotoTagsSync()
        val photoResult = photoRepository.getPhotos()
        photoResult.onSuccess { photos ->
            val photoMap = photos.associateBy { it.id }
            allTags.forEach { entity ->
                val people = parseJsonArray(entity.people)
                val matched = people.filter { person ->
                    matchingClusterIds.any { clusterId ->
                        val cluster = clusters.find { it.clusterId == clusterId }
                        cluster?.name?.lowercase()?.let { person.lowercase().contains(it) } == true
                    }
                }
                if (matched.isNotEmpty()) {
                    val photo = photoMap[entity.photoId]
                    if (photo != null) {
                        results.add(SearchResult.PhotoResult(photo, 1f, matched))
                    }
                }
            }
        }
        return results
    }

    private fun parseJsonArray(json: String): List<String> {
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { array.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
