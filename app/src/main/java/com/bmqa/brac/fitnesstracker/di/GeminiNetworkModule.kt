package com.bmqa.brac.fitnesstracker.di

import com.bmqa.brac.fitnesstracker.data.remote.datasource.GeminiFoodAnalysisDataSource
import com.bmqa.brac.fitnesstracker.data.remote.datasource.GeminiFoodAnalysisDataSourceImpl
import com.bmqa.brac.fitnesstracker.data.repository.GeminiFoodAnalysisRepositoryImpl
import com.bmqa.brac.fitnesstracker.domain.repository.GeminiFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.usecase.GeminiFoodAnalysisUseCase
import com.bmqa.brac.fitnesstracker.domain.service.ImageProcessingService
import com.bmqa.brac.fitnesstracker.data.service.ImageProcessingServiceImpl
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GeminiNetworkModule {
    
    @Binds
    @Singleton
    abstract fun bindGeminiFoodAnalysisDataSource(
        impl: GeminiFoodAnalysisDataSourceImpl
    ): GeminiFoodAnalysisDataSource
    
    @Binds
    @Singleton
    abstract fun bindGeminiFoodAnalysisRepository(
        impl: GeminiFoodAnalysisRepositoryImpl
    ): GeminiFoodAnalysisRepository
}

@Module
@InstallIn(SingletonComponent::class)
object GeminiProvidesModule {
    
    @Provides
    @Singleton
    fun provideGeminiFoodAnalysisUseCase(
        repository: GeminiFoodAnalysisRepository,
        imageProcessingService: ImageProcessingService
    ): GeminiFoodAnalysisUseCase {
        return GeminiFoodAnalysisUseCase(repository, imageProcessingService)
    }
}
