package com.bmqa.brac.fitnesstracker.di

import com.bmqa.brac.fitnesstracker.data.remote.api.OpenRouterApiService
import com.bmqa.brac.fitnesstracker.data.remote.datasource.OpenRouterRemoteDataSource
import com.bmqa.brac.fitnesstracker.data.remote.datasource.OpenRouterRemoteDataSourceImpl
import com.bmqa.brac.fitnesstracker.data.repository.OpenRouterRepositoryImpl
import com.bmqa.brac.fitnesstracker.domain.repository.OpenRouterRepository
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
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor

@Module
@InstallIn(SingletonComponent::class)
object OpenRouterNetworkModule {
    
    @Provides
    @Singleton
    @Named("OpenRouter")
    fun provideOpenRouterOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createLoggingInterceptor())
            .addInterceptor(createAuthInterceptor())
            .addInterceptor(OkHttpProfilerInterceptor())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    @Named("OpenRouter")
    fun provideOpenRouterRetrofit(@Named("OpenRouter") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://openrouter.ai/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideOpenRouterApiService(@Named("OpenRouter") retrofit: Retrofit): OpenRouterApiService {
        return retrofit.create(OpenRouterApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideOpenRouterRemoteDataSource(apiService: OpenRouterApiService): OpenRouterRemoteDataSource {
        return OpenRouterRemoteDataSourceImpl(apiService)
    }
    
    @Provides
    @Singleton
    fun provideOpenRouterRepository(remoteDataSource: OpenRouterRemoteDataSource): OpenRouterRepository {
        return OpenRouterRepositoryImpl(remoteDataSource)
    }
    
    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
    private fun createAuthInterceptor(): okhttp3.Interceptor {
        return okhttp3.Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer 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")
                .build()
            chain.proceed(newRequest)
        }
    }
}
