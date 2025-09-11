package com.bmqa.brac.foodlens.di

import com.bmqa.brac.foodlens.data.remote.network.KtorClient
import com.bmqa.brac.foodlens.data.repository.GeminiFoodAnalysisRepositoryImpl
import com.bmqa.brac.foodlens.data.repository.MockGeminiFoodAnalysisRepositoryImpl
import com.bmqa.brac.foodlens.data.local.repository.LocalFoodAnalysisRepository
import com.bmqa.brac.foodlens.domain.repository.GeminiFoodAnalysisRepository
import com.bmqa.brac.foodlens.domain.repository.FoodAnalysisRepository
import com.bmqa.brac.foodlens.domain.usecase.GeminiFoodAnalysisUseCase
import com.bmqa.brac.foodlens.domain.usecase.SaveFoodAnalysisUseCase
import com.bmqa.brac.foodlens.domain.service.ImageProcessingService
import com.bmqa.brac.foodlens.data.service.ImageProcessingServiceImpl
import com.bmqa.brac.foodlens.common.constants.GeminiConstants
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