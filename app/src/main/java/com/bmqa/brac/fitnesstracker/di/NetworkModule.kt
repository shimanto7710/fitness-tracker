package com.bmqa.brac.fitnesstracker.di

import com.bmqa.brac.fitnesstracker.data.local.datasource.FoodNutritionDataSource
import com.bmqa.brac.fitnesstracker.data.local.datasource.FoodNutritionDataSourceImpl
import com.bmqa.brac.fitnesstracker.data.remote.api.ClarifaiApiService
import com.bmqa.brac.fitnesstracker.data.remote.api.CustomFoodDetectionApiService
import com.bmqa.brac.fitnesstracker.data.remote.datasource.ClarifaiRemoteDataSource
import com.bmqa.brac.fitnesstracker.data.remote.datasource.ClarifaiRemoteDataSourceImpl
import com.bmqa.brac.fitnesstracker.data.remote.datasource.CustomFoodDetectionDataSource
import com.bmqa.brac.fitnesstracker.data.remote.datasource.CustomFoodDetectionDataSourceImpl
import com.bmqa.brac.fitnesstracker.data.remote.datasource.GeminiFoodAnalysisDataSource
import com.bmqa.brac.fitnesstracker.data.remote.datasource.GeminiFoodAnalysisDataSourceImpl
import com.bmqa.brac.fitnesstracker.data.remote.network.RetrofitClient
import com.bmqa.brac.fitnesstracker.data.repository.ClarifaiRepositoryImpl
import com.bmqa.brac.fitnesstracker.data.repository.CustomFoodDetectionRepositoryImpl
import com.bmqa.brac.fitnesstracker.data.repository.GeminiFoodAnalysisRepositoryImpl
import com.bmqa.brac.fitnesstracker.data.mapper.ClarifaiMapper
import com.bmqa.brac.fitnesstracker.data.service.ImageProcessingServiceImpl
import com.bmqa.brac.fitnesstracker.domain.repository.ClarifaiRepository
import com.bmqa.brac.fitnesstracker.domain.repository.CustomFoodDetectionRepository
import com.bmqa.brac.fitnesstracker.domain.repository.GeminiFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.service.ImageProcessingService
import com.bmqa.brac.fitnesstracker.domain.usecase.RecognizeFoodUseCase
import com.bmqa.brac.fitnesstracker.domain.usecase.CustomFoodDetectionUseCase
import com.bmqa.brac.fitnesstracker.domain.usecase.GeminiFoodAnalysisUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    
    // Data Source Bindings
    @Binds
    @Singleton
    abstract fun bindFoodNutritionDataSource(
        impl: FoodNutritionDataSourceImpl
    ): FoodNutritionDataSource
    
    @Binds
    @Singleton
    abstract fun bindClarifaiRemoteDataSource(
        impl: ClarifaiRemoteDataSourceImpl
    ): ClarifaiRemoteDataSource
    
    @Binds
    @Singleton
    abstract fun bindCustomFoodDetectionDataSource(
        impl: CustomFoodDetectionDataSourceImpl
    ): CustomFoodDetectionDataSource
    
    @Binds
    @Singleton
    abstract fun bindGeminiFoodAnalysisDataSource(
        impl: GeminiFoodAnalysisDataSourceImpl
    ): GeminiFoodAnalysisDataSource
    
    // Repository Bindings
    @Binds
    @Singleton
    abstract fun bindClarifaiRepository(
        impl: ClarifaiRepositoryImpl
    ): ClarifaiRepository
    
    @Binds
    @Singleton
    abstract fun bindCustomFoodDetectionRepository(
        impl: CustomFoodDetectionRepositoryImpl
    ): CustomFoodDetectionRepository
    
    @Binds
    @Singleton
    abstract fun bindGeminiFoodAnalysisRepository(
        impl: GeminiFoodAnalysisRepositoryImpl
    ): GeminiFoodAnalysisRepository
    
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
    
    // OkHttp Client Providers
    @Provides
    @Singleton
    @Named("Clarifai")
    fun provideClarifaiOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // Add OkHttp Profiler interceptor for API monitoring
        val profilerInterceptor = com.localebro.okhttpprofiler.OkHttpProfilerInterceptor()
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(profilerInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    @Named("CustomFoodDetection")
    fun provideCustomFoodDetectionOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        // Add OkHttp Profiler interceptor for API monitoring
        val profilerInterceptor = com.localebro.okhttpprofiler.OkHttpProfilerInterceptor()
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(profilerInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    // Retrofit Client Providers
    @Provides
    @Singleton
    fun provideRetrofitClient(): RetrofitClient {
        return RetrofitClient()
    }
    
    @Provides
    @Singleton
    @Named("Clarifai")
    fun provideClarifaiRetrofit(@Named("Clarifai") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.clarifai.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    @Named("CustomFoodDetection")
    fun provideCustomFoodDetectionRetrofit(@Named("CustomFoodDetection") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://cuddly-umbrella-rr7qvrgq5g92xx7p-8000.app.github.dev/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // API Service Providers
    @Provides
    @Singleton
    fun provideClarifaiApiService(retrofitClient: RetrofitClient): ClarifaiApiService {
        return retrofitClient.createService(ClarifaiApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideCustomFoodDetectionApiService(@Named("CustomFoodDetection") retrofit: Retrofit): CustomFoodDetectionApiService {
        return retrofit.create(CustomFoodDetectionApiService::class.java)
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
    fun provideCustomFoodDetectionUseCase(
        repository: CustomFoodDetectionRepository,
        imageProcessingService: ImageProcessingService
    ): CustomFoodDetectionUseCase {
        return CustomFoodDetectionUseCase(repository, imageProcessingService)
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
