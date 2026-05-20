package com.retrivai.app.domain.usecase.face

import com.retrivai.app.domain.repository.FaceClusterRepository
import javax.inject.Inject

class RenameFaceUseCase @Inject constructor(
    private val faceClusterRepository: FaceClusterRepository
) {
    suspend operator fun invoke(clusterId: Long, name: String?) {
        faceClusterRepository.updateFaceClusterName(clusterId, name)
    }
}
