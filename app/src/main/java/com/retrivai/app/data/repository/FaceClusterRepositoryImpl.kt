package com.retrivai.app.data.repository

import com.retrivai.app.data.local.database.FaceClusterEntity
import com.retrivai.app.data.local.database.dao.FaceClusterDao
import com.retrivai.app.domain.model.FaceCluster
import com.retrivai.app.domain.repository.FaceClusterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject

class FaceClusterRepositoryImpl @Inject constructor(
    private val faceClusterDao: FaceClusterDao
) : FaceClusterRepository {

    override fun getFaceCluster(clusterId: Long): Flow<FaceCluster?> {
        return faceClusterDao.getFaceCluster(clusterId).map { entity ->
            entity?.toDomainModel()
        }
    }

    override fun getAllFaceClusters(): Flow<List<FaceCluster>> {
        return faceClusterDao.getAllFaceClusters().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getNamedFaceClusters(): Flow<List<FaceCluster>> {
        return faceClusterDao.getNamedFaceClusters().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun saveFaceCluster(cluster: FaceCluster) {
        faceClusterDao.insertFaceCluster(cluster.toEntity())
    }

    override suspend fun saveFaceClusters(clusters: List<FaceCluster>) {
        faceClusterDao.insertFaceClusters(clusters.map { it.toEntity() })
    }

    override suspend fun updateFaceClusterName(clusterId: Long, name: String?) {
        faceClusterDao.updateFaceClusterName(clusterId, name, System.currentTimeMillis())
    }

    override suspend fun deleteFaceCluster(clusterId: Long) {
        faceClusterDao.deleteFaceCluster(clusterId)
    }

    override suspend fun deleteAllFaceClusters() {
        faceClusterDao.deleteAllFaceClusters()
    }

    override suspend fun getFaceClusterCount(): Int {
        return faceClusterDao.getFaceClusterCount()
    }

    private fun FaceClusterEntity.toDomainModel(): FaceCluster {
        return FaceCluster(
            clusterId = clusterId,
            faceCount = faceCount,
            embedding = parseJsonArray(embeddingJson),
            samplePhotoId = samplePhotoId,
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun FaceCluster.toEntity(): FaceClusterEntity {
        return FaceClusterEntity(
            clusterId = clusterId,
            faceCount = faceCount,
            embeddingJson = toJsonArray(embedding),
            samplePhotoId = samplePhotoId,
            name = name,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun parseJsonArray(json: String): List<Float> {
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { array.getDouble(it).toFloat() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun toJsonArray(list: List<Float>): String {
        return JSONArray(list.map { it.toDouble() }).toString()
    }
}
