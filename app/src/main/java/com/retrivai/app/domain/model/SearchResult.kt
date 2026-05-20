package com.retrivai.app.domain.model

sealed class SearchResult {
    abstract val relevanceScore: Float
    abstract val matchedTags: List<String>

    data class PhotoResult(
        val photo: Photo,
        override val relevanceScore: Float,
        override val matchedTags: List<String> = emptyList()
    ) : SearchResult()

    data class VideoResult(
        val video: Video,
        override val relevanceScore: Float,
        override val matchedTags: List<String> = emptyList()
    ) : SearchResult()
}
