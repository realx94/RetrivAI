package com.retrivai.app.domain.model

data class Album(
    val id: String,
    val title: String,
    val type: AlbumType,
    val coverPhotoIds: List<Long>,
    val itemCount: Int
)

enum class AlbumType {
    PEOPLE, PLACES, EVENTS, AI_SUGGESTED
}
