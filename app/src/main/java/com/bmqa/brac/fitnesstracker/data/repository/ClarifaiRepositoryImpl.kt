package com.bmqa.brac.fitnesstracker.data.repository

import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.data.mapper.ClarifaiMapper
import com.bmqa.brac.fitnesstracker.data.remote.api.ClarifaiApiService
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiResponse
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiUserAppId
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiInputRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiInputDataRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiImageRequest
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.domain.repository.ClarifaiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClarifaiRepositoryImpl @Inject constructor(
    private val apiService: ClarifaiApiService,
    private val clarifaiMapper: ClarifaiMapper
) : ClarifaiRepository {
    
    override suspend fun recognizeFoodFromBase64(base64Image: String): Result<List<FoodItem>> = withContext(Dispatchers.IO) {
        try {
            val request = ClarifaiRequest(
                user_app_id = ClarifaiUserAppId(
                    user_id = AppConstants.Api.USER_ID,
                    app_id = AppConstants.Api.APP_ID
                ),
                inputs = listOf(
                    ClarifaiInputRequest(
                        data = ClarifaiInputDataRequest(
                            image = ClarifaiImageRequest(base64 = base64Image)
                        )
                    )
                )
            )
            
            val response = apiService.recognizeFood(
                authorization = AppConstants.Api.AUTHORIZATION_HEADER,
                requestBody = request
            )
            
            if (response.isSuccessful) {
                val clarifaiResponse = response.body()!!
                Result.success(clarifaiResponse).map { response ->
                    clarifaiMapper.mapToFoodItems(response)
                }
            } else {
                Result.failure(Exception("API call failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
