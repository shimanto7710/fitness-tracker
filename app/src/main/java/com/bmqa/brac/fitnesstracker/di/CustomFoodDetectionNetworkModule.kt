package com.bmqa.brac.fitnesstracker.di

import com.bmqa.brac.fitnesstracker.data.remote.api.CustomFoodDetectionApiService
import com.bmqa.brac.fitnesstracker.data.remote.datasource.CustomFoodDetectionDataSource
import com.bmqa.brac.fitnesstracker.data.remote.datasource.CustomFoodDetectionDataSourceImpl
import com.bmqa.brac.fitnesstracker.data.repository.CustomFoodDetectionRepositoryImpl
import com.bmqa.brac.fitnesstracker.domain.repository.CustomFoodDetectionRepository
import com.bmqa.brac.fitnesstracker.domain.usecase.CustomFoodDetectionUseCase
import com.bmqa.brac.fitnesstracker.domain.service.ImageProcessingService
import com.bmqa.brac.fitnesstracker.data.service.ImageProcessingServiceImpl
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CustomFoodDetectionNetworkModule {
    
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
    
    @Provides
    @Singleton
    fun provideCustomFoodDetectionApiService(@Named("CustomFoodDetection") retrofit: Retrofit): CustomFoodDetectionApiService {
        return retrofit.create(CustomFoodDetectionApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideCustomFoodDetectionDataSource(apiService: CustomFoodDetectionApiService): CustomFoodDetectionDataSource {
        return CustomFoodDetectionDataSourceImpl(apiService)
    }
    
    @Provides
    @Singleton
    fun provideCustomFoodDetectionRepository(dataSource: CustomFoodDetectionDataSource): CustomFoodDetectionRepository {
        return CustomFoodDetectionRepositoryImpl(dataSource)
    }
    
    @Provides
    @Singleton
    fun provideImageProcessingService(context: Context): ImageProcessingService {
        return ImageProcessingServiceImpl(context)
    }
    
    @Provides
    @Singleton
    fun provideCustomFoodDetectionUseCase(
        repository: CustomFoodDetectionRepository,
        imageProcessingService: ImageProcessingService
    ): CustomFoodDetectionUseCase {
        return CustomFoodDetectionUseCase(repository, imageProcessingService)
    }
}
