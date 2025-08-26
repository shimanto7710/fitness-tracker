package com.bmqa.brac.fitnesstracker.data.remote.api

import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OpenRouterApiService {
    
    @POST("chat/completions")
    suspend fun analyzeFood(
        @Body request: OpenRouterRequest
    ): OpenRouterResponse
}
