package com.retrivai.app.di

import com.retrivai.app.ai.FaceAnalyzer
import com.retrivai.app.ai.FaceAnalyzerImpl
import com.retrivai.app.ai.GemmaAnalyzer
import com.retrivai.app.ai.GemmaAnalyzerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {

    @Binds
    @Singleton
    abstract fun bindGemmaAnalyzer(
        impl: GemmaAnalyzerImpl
    ): GemmaAnalyzer

    @Binds
    @Singleton
    abstract fun bindFaceAnalyzer(
        impl: FaceAnalyzerImpl
    ): FaceAnalyzer
}