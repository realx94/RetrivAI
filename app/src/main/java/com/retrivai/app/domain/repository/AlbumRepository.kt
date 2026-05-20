package com.retrivai.app.domain.repository

import com.retrivai.app.domain.model.Album
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    fun getAlbums(): Flow<List<Album>>
    fun getPeopleAlbums(): Flow<List<Album>>
    fun getPlacesAlbums(): Flow<List<Album>>
    fun getEventsAlbums(): Flow<List<Album>>
    fun getAiSuggestedAlbums(): Flow<List<Album>>
}
