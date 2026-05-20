package com.retrivai.app.data.repository

import com.retrivai.app.data.local.database.dao.FaceClusterDao
import com.retrivai.app.data.local.database.dao.PhotoTagDao
import com.retrivai.app.domain.model.Album
import com.retrivai.app.domain.model.AlbumType
import com.retrivai.app.domain.repository.AlbumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val photoTagDao: PhotoTagDao,
    private val faceClusterDao: FaceClusterDao
) : AlbumRepository {

    override fun getAlbums(): Flow<List<Album>> = combine(
        getPeopleAlbums(), getPlacesAlbums(), getEventsAlbums(), getAiSuggestedAlbums()
    ) { people, places, events, ai -> people + places + events + ai }

    override fun getPeopleAlbums(): Flow<List<Album>> =
        faceClusterDao.getNamedFaceClusters().map { clusters ->
            clusters.map { entity ->
                Album(
                    id = "people_${entity.clusterId}",
                    title = entity.name ?: "Person ${entity.clusterId}",
                    type = AlbumType.PEOPLE,
                    coverPhotoIds = listOf(entity.samplePhotoId),
                    itemCount = entity.faceCount
                )
            }
        }

    override fun getPlacesAlbums(): Flow<List<Album>> =
        photoTagDao.getAllPhotoTags().map { tags ->
            val grouped = mutableMapOf<String, MutableList<Long>>()
            tags.forEach { entity ->
                parseJsonArray(entity.locations).forEach { location ->
                    if (location.isNotBlank()) {
                        grouped.getOrPut(location) { mutableListOf() }.add(entity.photoId)
                    }
                }
            }
            grouped.map { (location, photoIds) ->
                Album(
                    id = "places_${location.hashCode()}",
                    title = location,
                    type = AlbumType.PLACES,
                    coverPhotoIds = photoIds.take(4),
                    itemCount = photoIds.size
                )
            }.sortedByDescending { it.itemCount }
        }

    override fun getEventsAlbums(): Flow<List<Album>> =
        photoTagDao.getAllPhotoTags().map { tags ->
            val grouped = mutableMapOf<String, MutableList<Long>>()
            tags.forEach { entity ->
                parseJsonArray(entity.events).forEach { event ->
                    if (event.isNotBlank()) {
                        grouped.getOrPut(event) { mutableListOf() }.add(entity.photoId)
                    }
                }
            }
            grouped.map { (event, photoIds) ->
                Album(
                    id = "events_${event.hashCode()}",
                    title = event,
                    type = AlbumType.EVENTS,
                    coverPhotoIds = photoIds.take(4),
                    itemCount = photoIds.size
                )
            }.sortedByDescending { it.itemCount }
        }

    override fun getAiSuggestedAlbums(): Flow<List<Album>> =
        photoTagDao.getAllPhotoTags().map { tags ->
            val grouped = mutableMapOf<String, MutableList<Long>>()
            tags.forEach { entity ->
                parseJsonArray(entity.tags).forEach { tag ->
                    if (tag.isNotBlank()) {
                        grouped.getOrPut(tag) { mutableListOf() }.add(entity.photoId)
                    }
                }
            }
            grouped
                .filter { (_, photoIds) -> photoIds.size >= 2 }
                .map { (tag, photoIds) ->
                    Album(
                        id = "ai_${tag.hashCode()}",
                        title = tag.replaceFirstChar { it.uppercase() },
                        type = AlbumType.AI_SUGGESTED,
                        coverPhotoIds = photoIds.take(4),
                        itemCount = photoIds.size
                    )
                }.sortedByDescending { it.itemCount }.take(20)
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
