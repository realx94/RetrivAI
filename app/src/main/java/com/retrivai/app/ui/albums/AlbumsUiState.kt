package com.retrivai.app.ui.albums

import com.retrivai.app.domain.model.Album

data class AlbumsUiState(
    val peopleAlbums: List<Album> = emptyList(),
    val placesAlbums: List<Album> = emptyList(),
    val eventsAlbums: List<Album> = emptyList(),
    val aiSuggestedAlbums: List<Album> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
