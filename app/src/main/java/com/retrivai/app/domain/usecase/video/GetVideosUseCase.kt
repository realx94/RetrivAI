package com.retrivai.app.domain.usecase.video

import com.retrivai.app.domain.model.Video
import com.retrivai.app.domain.repository.VideoRepository
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(): Result<List<Video>> {
        return videoRepository.getVideos()
    }
}