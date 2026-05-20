package com.retrivai.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.retrivai.app.domain.usecase.search.SearchPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPhotosUseCase: SearchPhotosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        observeQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        queryFlow
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    _uiState.update { it.copy(results = emptyList(), isSearching = false, isEmpty = false) }
                    return@onEach
                }
                _uiState.update { it.copy(isSearching = true, error = null) }
                searchPhotosUseCase(query)
                    .onEach { results ->
                        _uiState.update {
                            it.copy(
                                results = results,
                                isSearching = false,
                                isEmpty = results.isEmpty()
                            )
                        }
                    }
                    .launchIn(viewModelScope)
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
        queryFlow.value = query
    }

    fun clearQuery() {
        _uiState.update { SearchUiState() }
        queryFlow.value = ""
    }
}
