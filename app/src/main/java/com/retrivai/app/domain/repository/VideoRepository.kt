package com.retrivai.app.domain.repository

import com.retrivai.app.domain.model.Video

interface VideoRepository {
    suspend fun getVideos(): Result<List<Video>>
}