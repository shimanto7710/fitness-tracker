package com.bmqa.brac.fitnesstracker.data.remote.api

import com.bmqa.brac.fitnesstracker.data.remote.dto.CustomFoodDetectionResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CustomFoodDetectionApiService {
    
    @Multipart
    @POST("predict")
    suspend fun detectFood(
        @Part imageFile: MultipartBody.Part
    ): CustomFoodDetectionResponse
}
