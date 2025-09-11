package com.rookie.code.di

import com.rookie.code.data.remote.network.KtorClient
import com.rookie.code.data.repository.GeminiFoodAnalysisRepositoryImpl
import com.rookie.code.data.repository.MockGeminiFoodAnalysisRepositoryImpl
import com.rookie.code.data.local.repository.LocalFoodAnalysisRepository
import com.rookie.code.domain.repository.GeminiFoodAnalysisRepository
import com.rookie.code.domain.repository.FoodAnalysisRepository
import com.rookie.code.domain.usecase.GeminiFoodAnalysisUseCase
import com.rookie.code.domain.usecase.SaveFoodAnalysisUseCase
import com.rookie.code.domain.service.ImageProcessingService
import com.rookie.code.data.service.ImageProcessingServiceImpl
import com.rookie.code.common.constants.GeminiConstants
import io.ktor.client.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    
    // Ktor Client
    single { KtorClient() }
    
    
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
    single<FoodAnalysisRepository> { get<LocalFoodAnalysisRepository>() }
    
    // Use Cases
    single { GeminiFoodAnalysisUseCase(get(), get()) }
    single { SaveFoodAnalysisUseCase(get()) }
}