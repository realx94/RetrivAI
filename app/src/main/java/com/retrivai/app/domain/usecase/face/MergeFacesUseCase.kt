package com.retrivai.app.domain.usecase.face

import com.retrivai.app.domain.repository.FaceClusterRepository
import javax.inject.Inject

class MergeFacesUseCase @Inject constructor(
    private val faceClusterRepository: FaceClusterRepository
) {
    /**
     * Merges [sourceClusterId] into [targetClusterId]: renames target to [targetName] if provided,
     * then deletes the source cluster.
     */
    suspend operator fun invoke(targetClusterId: Long, sourceClusterId: Long, targetName: String?) {
        if (targetName != null) {
            faceClusterRepository.updateFaceClusterName(targetClusterId, targetName)
        }
        faceClusterRepository.deleteFaceCluster(sourceClusterId)
    }
}
