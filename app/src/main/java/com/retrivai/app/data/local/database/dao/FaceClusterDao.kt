package com.retrivai.app.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.retrivai.app.data.local.database.FaceClusterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FaceClusterDao {

    @Query("SELECT * FROM face_clusters WHERE clusterId = :clusterId")
    fun getFaceCluster(clusterId: Long): Flow<FaceClusterEntity?>

    @Query("SELECT * FROM face_clusters WHERE clusterId = :clusterId")
    suspend fun getFaceClusterSync(clusterId: Long): FaceClusterEntity?

    @Query("SELECT * FROM face_clusters")
    fun getAllFaceClusters(): Flow<List<FaceClusterEntity>>

    @Query("SELECT * FROM face_clusters")
    suspend fun getAllFaceClustersSync(): List<FaceClusterEntity>

    @Query("SELECT * FROM face_clusters WHERE name IS NOT NULL")
    fun getNamedFaceClusters(): Flow<List<FaceClusterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaceCluster(cluster: FaceClusterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaceClusters(clusters: List<FaceClusterEntity>)

    @Update
    suspend fun updateFaceCluster(cluster: FaceClusterEntity)

    @Query("UPDATE face_clusters SET name = :name, updatedAt = :updatedAt WHERE clusterId = :clusterId")
    suspend fun updateFaceClusterName(clusterId: Long, name: String?, updatedAt: Long)

    @Query("DELETE FROM face_clusters WHERE clusterId = :clusterId")
    suspend fun deleteFaceCluster(clusterId: Long)

    @Query("DELETE FROM face_clusters")
    suspend fun deleteAllFaceClusters()

    @Query("SELECT COUNT(*) FROM face_clusters")
    suspend fun getFaceClusterCount(): Int
}