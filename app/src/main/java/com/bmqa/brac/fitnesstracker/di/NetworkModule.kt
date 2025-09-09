package com.bmqa.brac.fitnesstracker.di

import com.bmqa.brac.fitnesstracker.data.remote.api.ClarifaiKtorApiService
import com.bmqa.brac.fitnesstracker.data.remote.network.KtorClient
import com.bmqa.brac.fitnesstracker.data.repository.ClarifaiRepositoryImpl
import com.bmqa.brac.fitnesstracker.data.repository.GeminiFoodAnalysisRepositoryImpl
import com.bmqa.brac.fitnesstracker.data.repository.MockGeminiFoodAnalysisRepositoryImpl
import com.bmqa.brac.fitnesstracker.data.local.repository.LocalFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.data.mapper.ClarifaiMapper
import com.bmqa.brac.fitnesstracker.domain.repository.ClarifaiRepository
import com.bmqa.brac.fitnesstracker.domain.repository.GeminiFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.usecase.RecognizeFoodUseCase
import com.bmqa.brac.fitnesstracker.domain.usecase.GeminiFoodAnalysisUseCase
import com.bmqa.brac.fitnesstracker.domain.service.ImageProcessingService
import com.bmqa.brac.fitnesstracker.data.service.ImageProcessingServiceImpl
import com.bmqa.brac.fitnesstracker.common.constants.GeminiConstants
import io.ktor.client.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    
    // Ktor Client
    single { KtorClient() }
    
    // HttpClient
    single<HttpClient> { get<KtorClient>().createClarifaiClient() }
    
    // Mapper
    single { ClarifaiMapper() }
    
    // API Service
    single { ClarifaiKtorApiService(get()) }
    
    // Repository implementations
    single<ClarifaiRepository> { ClarifaiRepositoryImpl(get(), get()) }
    
    // Gemini Repository - conditional injection based on mock flag
    single<GeminiFoodAnalysisRepository> {
        if (GeminiConstants.USE_MOCK_DATA) {
            MockGeminiFoodAnalysisRepositoryImpl()
        } else {
            GeminiFoodAnalysisRepositoryImpl(androidContext())
        }
    }
    
    // Services
    single<ImageProcessingService> { ImageProcessingServiceImpl(androidContext()) }
    
    // Local Repository
    single { LocalFoodAnalysisRepository(androidContext()) }
    
    // Use Cases
    single { RecognizeFoodUseCase(get()) }
    single { GeminiFoodAnalysisUseCase(get(), get()) }
}