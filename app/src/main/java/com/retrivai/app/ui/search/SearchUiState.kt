package com.retrivai.app.ui.search

import com.retrivai.app.domain.model.SearchResult

data class SearchUiState(
    val query: String = "",
    val results: List<SearchResult> = emptyList(),
    val isSearching: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String? = null
)
