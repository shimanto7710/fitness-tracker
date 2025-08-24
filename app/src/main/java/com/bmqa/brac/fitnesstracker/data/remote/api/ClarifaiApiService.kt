package com.bmqa.brac.fitnesstracker.data.remote.api

import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiResponse
import retrofit2.Response
import retrofit2.http.*

interface ClarifaiApiService {
    
    @POST("v2/models/${AppConstants.Api.MODEL_ID}/outputs")
    suspend fun recognizeFood(
        @Header("Authorization") authorization: String,
        @Body requestBody: ClarifaiRequest
    ): Response<ClarifaiResponse>
}
