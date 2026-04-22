package com.retrivai.app.domain.repository

import com.retrivai.app.domain.model.Photo

interface PhotoRepository {
    suspend fun getPhotos(): Result<List<Photo>>
}
