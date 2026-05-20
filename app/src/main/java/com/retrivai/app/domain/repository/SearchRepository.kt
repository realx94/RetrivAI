package com.retrivai.app.domain.repository

import com.retrivai.app.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(query: String): Flow<List<SearchResult>>
    suspend fun searchByPersonName(name: String): List<SearchResult>
}
