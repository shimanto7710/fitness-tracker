package com.bmqa.brac.fitnesstracker.di

import com.bmqa.brac.fitnesstracker.data.local.datasource.FoodNutritionDataSource
import com.bmqa.brac.fitnesstracker.data.local.datasource.FoodNutritionDataSourceImpl
import com.bmqa.brac.fitnesstracker.data.remote.api.ClarifaiApiService
import com.bmqa.brac.fitnesstracker.data.remote.datasource.ClarifaiRemoteDataSource
import com.bmqa.brac.fitnesstracker.data.remote.datasource.ClarifaiRemoteDataSourceImpl
import com.bmqa.brac.fitnesstracker.data.remote.network.RetrofitClient
import com.bmqa.brac.fitnesstracker.data.repository.ClarifaiRepositoryImpl
import com.bmqa.brac.fitnesstracker.data.mapper.ClarifaiMapper
import com.bmqa.brac.fitnesstracker.domain.repository.ClarifaiRepository
import com.bmqa.brac.fitnesstracker.domain.usecase.RecognizeFoodUseCase
import com.bmqa.brac.fitnesstracker.presentation.service.ClarifaiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    
    @Binds
    @Singleton
    abstract fun bindFoodNutritionDataSource(
        impl: FoodNutritionDataSourceImpl
    ): FoodNutritionDataSource
    
    @Binds
    @Singleton
    abstract fun bindClarifaiRepository(
        impl: ClarifaiRepositoryImpl
    ): ClarifaiRepository
    
    @Binds
    @Singleton
    abstract fun bindClarifaiRemoteDataSource(
        impl: ClarifaiRemoteDataSourceImpl
    ): ClarifaiRemoteDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvidesModule {
    
    @Provides
    @Singleton
    fun provideClarifaiMapper(): ClarifaiMapper {
        return ClarifaiMapper()
    }
    
    @Provides
    @Singleton
    fun provideRetrofitClient(): RetrofitClient {
        return RetrofitClient()
    }
    
    @Provides
    @Singleton
    fun provideClarifaiApiService(retrofitClient: RetrofitClient): ClarifaiApiService {
        return retrofitClient.createService(ClarifaiApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideRecognizeFoodUseCase(
        repository: ClarifaiRepository
    ): RecognizeFoodUseCase {
        return RecognizeFoodUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideClarifaiService(
        useCase: RecognizeFoodUseCase
    ): ClarifaiService {
        return ClarifaiService(useCase)
    }
}
