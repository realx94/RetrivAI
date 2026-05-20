package com.retrivai.app.di

import android.content.Context
import androidx.room.Room
import com.retrivai.app.data.local.database.AppDatabase
import com.retrivai.app.data.local.database.dao.FaceClusterDao
import com.retrivai.app.data.local.database.dao.IndexingStateDao
import com.retrivai.app.data.local.database.dao.PhotoTagDao
import com.retrivai.app.data.local.database.dao.VideoTagDao
import com.retrivai.app.data.repository.FaceClusterRepositoryImpl
import com.retrivai.app.data.repository.IndexingRepositoryImpl
import com.retrivai.app.data.repository.PhotoTagRepositoryImpl
import com.retrivai.app.data.repository.VideoTagRepositoryImpl
import com.retrivai.app.domain.repository.FaceClusterRepository
import com.retrivai.app.domain.repository.IndexingRepository
import com.retrivai.app.domain.repository.PhotoTagRepository
import com.retrivai.app.domain.repository.VideoTagRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "retrivai_database"
        ).build()
    }

    @Provides
    fun provideIndexingStateDao(database: AppDatabase): IndexingStateDao {
        return database.indexingStateDao()
    }

    @Provides
    @Singleton
    fun provideIndexingRepository(
        indexingStateDao: IndexingStateDao
    ): IndexingRepository {
        return IndexingRepositoryImpl(indexingStateDao)
    }

    @Provides
    fun providePhotoTagDao(database: AppDatabase): PhotoTagDao {
        return database.photoTagDao()
    }

    @Provides
    @Singleton
    fun providePhotoTagRepository(
        photoTagDao: PhotoTagDao
    ): PhotoTagRepository {
        return PhotoTagRepositoryImpl(photoTagDao)
    }

    @Provides
    fun provideVideoTagDao(database: AppDatabase): VideoTagDao {
        return database.videoTagDao()
    }

    @Provides
    @Singleton
    fun provideVideoTagRepository(
        videoTagDao: VideoTagDao
    ): VideoTagRepository {
        return VideoTagRepositoryImpl(videoTagDao)
    }

    @Provides
    fun provideFaceClusterDao(database: AppDatabase): FaceClusterDao {
        return database.faceClusterDao()
    }

    @Provides
    @Singleton
    fun provideFaceClusterRepository(
        faceClusterDao: FaceClusterDao
    ): FaceClusterRepository {
        return FaceClusterRepositoryImpl(faceClusterDao)
    }
}