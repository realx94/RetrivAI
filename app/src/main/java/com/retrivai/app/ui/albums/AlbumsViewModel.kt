package com.retrivai.app.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retrivai.app.domain.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumsUiState())
    val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

    init {
        observeAlbums()
    }

    private fun observeAlbums() {
        combine(
            albumRepository.getPeopleAlbums(),
            albumRepository.getPlacesAlbums(),
            albumRepository.getEventsAlbums(),
            albumRepository.getAiSuggestedAlbums()
        ) { people, places, events, ai ->
            AlbumsUiState(
                peopleAlbums = people,
                placesAlbums = places,
                eventsAlbums = events,
                aiSuggestedAlbums = ai,
                isLoading = false
            )
        }
            .onEach { state -> _uiState.value = state }
            .launchIn(viewModelScope)
    }
}
