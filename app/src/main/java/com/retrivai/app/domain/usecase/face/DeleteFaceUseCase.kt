package com.retrivai.app.domain.usecase.face

import com.retrivai.app.domain.repository.FaceClusterRepository
import javax.inject.Inject

class DeleteFaceUseCase @Inject constructor(
    private val faceClusterRepository: FaceClusterRepository
) {
    suspend operator fun invoke(clusterId: Long) {
        faceClusterRepository.deleteFaceCluster(clusterId)
    }
}
