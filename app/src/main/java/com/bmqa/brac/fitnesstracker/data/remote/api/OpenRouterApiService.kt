package com.bmqa.brac.fitnesstracker.data.remote.api

import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OpenRouterApiService {
    
    @Multipart
    @POST("chat/completions")
    suspend fun analyzeFood(
        @Part("model") model: String,
        @Part("messages[0][role]") role: String,
        @Part("messages[0][content][0][type]") textType: String,
        @Part("messages[0][content][0][text]") text: String,
        @Part("messages[0][content][1][type]") imageType: String,
        @Part("messages[0][content][1][image_url][url]") imageUrl: String
    ): OpenRouterResponse
}
