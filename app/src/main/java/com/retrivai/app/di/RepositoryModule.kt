package com.retrivai.app.di

import com.retrivai.app.data.repository.AlbumRepositoryImpl
import com.retrivai.app.data.repository.PhotoRepositoryImpl
import com.retrivai.app.data.repository.SearchRepositoryImpl
import com.retrivai.app.data.repository.VideoRepositoryImpl
import com.retrivai.app.domain.repository.AlbumRepository
import com.retrivai.app.domain.repository.PhotoRepository
import com.retrivai.app.domain.repository.SearchRepository
import com.retrivai.app.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(
        photoRepositoryImpl: PhotoRepositoryImpl
    ): PhotoRepository

    @Binds
    @Singleton
    abstract fun bindVideoRepository(
        videoRepositoryImpl: VideoRepositoryImpl
    ): VideoRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindAlbumRepository(
        albumRepositoryImpl: AlbumRepositoryImpl
    ): AlbumRepository
}
