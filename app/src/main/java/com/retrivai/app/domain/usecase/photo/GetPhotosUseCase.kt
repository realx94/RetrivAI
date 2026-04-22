package com.retrivai.app.domain.usecase.photo

import com.retrivai.app.domain.model.Photo
import com.retrivai.app.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(): Result<List<Photo>> {
        return photoRepository.getPhotos()
    }
}
