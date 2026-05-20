package com.retrivai.app.domain.usecase.face

import com.retrivai.app.domain.model.FaceCluster
import com.retrivai.app.domain.repository.FaceClusterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFaceClustersUseCase @Inject constructor(
    private val faceClusterRepository: FaceClusterRepository
) {
    operator fun invoke(): Flow<List<FaceCluster>> {
        return faceClusterRepository.getAllFaceClusters()
    }
}
