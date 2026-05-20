package com.retrivai.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.retrivai.app.data.local.database.dao.FaceClusterDao
import com.retrivai.app.data.local.database.dao.IndexingStateDao
import com.retrivai.app.data.local.database.dao.PhotoTagDao
import com.retrivai.app.data.local.database.dao.VideoTagDao

@Database(
    entities = [IndexingStateEntity::class, PhotoTagEntity::class, VideoTagEntity::class, FaceClusterEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun indexingStateDao(): IndexingStateDao
    abstract fun photoTagDao(): PhotoTagDao
    abstract fun videoTagDao(): VideoTagDao
    abstract fun faceClusterDao(): FaceClusterDao
}