package com.retrivai.app.domain.usecase.search

import com.retrivai.app.domain.model.SearchResult
import com.retrivai.app.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPhotosUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(query: String): Flow<List<SearchResult>> {
        return searchRepository.search(query)
    }
}
