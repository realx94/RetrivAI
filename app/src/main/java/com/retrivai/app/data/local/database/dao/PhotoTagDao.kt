package com.retrivai.app.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.retrivai.app.data.local.database.PhotoTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoTagDao {

    @Query("SELECT * FROM photo_tags WHERE photoId = :photoId")
    fun getPhotoTag(photoId: Long): Flow<PhotoTagEntity?>

    @Query("SELECT * FROM photo_tags WHERE photoId = :photoId")
    suspend fun getPhotoTagSync(photoId: Long): PhotoTagEntity?

    @Query("SELECT * FROM photo_tags")
    fun getAllPhotoTags(): Flow<List<PhotoTagEntity>>

    @Query("SELECT * FROM photo_tags")
    suspend fun getAllPhotoTagsSync(): List<PhotoTagEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotoTag(photoTag: PhotoTagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotoTags(photoTags: List<PhotoTagEntity>)

    @Query("DELETE FROM photo_tags WHERE photoId = :photoId")
    suspend fun deletePhotoTag(photoId: Long)

    @Query("DELETE FROM photo_tags")
    suspend fun deleteAllPhotoTags()
}