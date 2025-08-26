package com.bmqa.brac.fitnesstracker.data.remote.datasource

import com.bmqa.brac.fitnesstracker.data.remote.api.OpenRouterApiService
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse
import javax.inject.Inject

interface OpenRouterRemoteDataSource {
    suspend fun analyzeFood(request: OpenRouterRequest): Result<OpenRouterResponse>
}

class OpenRouterRemoteDataSourceImpl @Inject constructor(
    private val apiService: OpenRouterApiService
) : OpenRouterRemoteDataSource {
    
    override suspend fun analyzeFood(request: OpenRouterRequest): Result<OpenRouterResponse> {
        return try {
            val response = apiService.analyzeFood(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
