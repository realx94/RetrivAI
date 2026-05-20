package com.retrivai.app.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.retrivai.app.data.local.database.VideoTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoTagDao {

    @Query("SELECT * FROM video_tags WHERE videoId = :videoId")
    fun getVideoTag(videoId: Long): Flow<VideoTagEntity?>

    @Query("SELECT * FROM video_tags WHERE videoId = :videoId")
    suspend fun getVideoTagSync(videoId: Long): VideoTagEntity?

    @Query("SELECT * FROM video_tags")
    fun getAllVideoTags(): Flow<List<VideoTagEntity>>

    @Query("SELECT * FROM video_tags")
    suspend fun getAllVideoTagsSync(): List<VideoTagEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoTag(videoTag: VideoTagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideoTags(videoTags: List<VideoTagEntity>)

    @Query("DELETE FROM video_tags WHERE videoId = :videoId")
    suspend fun deleteVideoTag(videoId: Long)

    @Query("DELETE FROM video_tags")
    suspend fun deleteAllVideoTags()
}