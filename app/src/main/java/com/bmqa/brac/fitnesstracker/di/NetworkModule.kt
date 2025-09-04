package com.bmqa.brac.fitnesstracker.di

import com.bmqa.brac.fitnesstracker.data.remote.api.ClarifaiKtorApiService
import com.bmqa.brac.fitnesstracker.data.remote.network.KtorClient
import io.ktor.client.*
import com.bmqa.brac.fitnesstracker.data.repository.ClarifaiRepositoryImpl
import com.bmqa.brac.fitnesstracker.data.repository.GeminiFoodAnalysisRepositoryImpl
import com.bmqa.brac.fitnesstracker.data.repository.MockGeminiFoodAnalysisRepositoryImpl
import com.bmqa.brac.fitnesstracker.common.constants.GeminiConstants
import com.bmqa.brac.fitnesstracker.data.mapper.ClarifaiMapper
import com.bmqa.brac.fitnesstracker.data.service.ImageProcessingServiceImpl
import com.bmqa.brac.fitnesstracker.domain.repository.ClarifaiRepository
import com.bmqa.brac.fitnesstracker.domain.repository.GeminiFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.service.ImageProcessingService
import com.bmqa.brac.fitnesstracker.domain.usecase.RecognizeFoodUseCase
import com.bmqa.brac.fitnesstracker.domain.usecase.GeminiFoodAnalysisUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    
    // Repository Bindings
    @Binds
    @Singleton
    abstract fun bindClarifaiRepository(
        impl: ClarifaiRepositoryImpl
    ): ClarifaiRepository
    
    
    // Service Bindings
    @Binds
    @Singleton
    abstract fun bindImageProcessingService(
        impl: ImageProcessingServiceImpl
    ): ImageProcessingService
    
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvidesModule {
    
    // Context Provider
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
    
    // Mapper Provider
    @Provides
    @Singleton
    fun provideClarifaiMapper(): ClarifaiMapper {
        return ClarifaiMapper()
    }
    
    // Ktor Client Provider
    @Provides
    @Singleton
    fun provideKtorClient(): KtorClient {
        return KtorClient()
    }
    
    // HttpClient Provider
    @Provides
    @Singleton
    @Named("Clarifai")
    fun provideClarifaiHttpClient(ktorClient: KtorClient): HttpClient {
        return ktorClient.createClarifaiClient()
    }
    
    // API Service Provider
    @Provides
    @Singleton
    fun provideClarifaiKtorApiService(@Named("Clarifai") httpClient: HttpClient): ClarifaiKtorApiService {
        return ClarifaiKtorApiService(httpClient)
    }
    
    
    // Use Case Providers
    @Provides
    @Singleton
    fun provideRecognizeFoodUseCase(
        repository: ClarifaiRepository
    ): RecognizeFoodUseCase {
        return RecognizeFoodUseCase(repository)
    }
    
    
    @Provides
    @Singleton
    fun provideGeminiFoodAnalysisRepository(
        realRepository: GeminiFoodAnalysisRepositoryImpl,
        mockRepository: MockGeminiFoodAnalysisRepositoryImpl
    ): GeminiFoodAnalysisRepository {
        return if (GeminiConstants.USE_MOCK_DATA) {
            mockRepository
        } else {
            realRepository
        }
    }
    
    @Provides
    @Singleton
    fun provideGeminiFoodAnalysisUseCase(
        repository: GeminiFoodAnalysisRepository,
        imageProcessingService: ImageProcessingService
    ): GeminiFoodAnalysisUseCase {
        return GeminiFoodAnalysisUseCase(repository, imageProcessingService)
    }
    
}
