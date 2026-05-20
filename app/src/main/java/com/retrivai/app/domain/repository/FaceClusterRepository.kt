package com.retrivai.app.domain.repository

import com.retrivai.app.domain.model.FaceCluster
import kotlinx.coroutines.flow.Flow

interface FaceClusterRepository {
    fun getFaceCluster(clusterId: Long): Flow<FaceCluster?>
    fun getAllFaceClusters(): Flow<List<FaceCluster>>
    fun getNamedFaceClusters(): Flow<List<FaceCluster>>
    suspend fun saveFaceCluster(cluster: FaceCluster)
    suspend fun saveFaceClusters(clusters: List<FaceCluster>)
    suspend fun updateFaceClusterName(clusterId: Long, name: String?)
    suspend fun deleteFaceCluster(clusterId: Long)
    suspend fun deleteAllFaceClusters()
    suspend fun getFaceClusterCount(): Int
}
