package com.bmqa.brac.fitnesstracker.data.repository

import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.data.mapper.ClarifaiMapper
import com.bmqa.brac.fitnesstracker.data.remote.api.ClarifaiKtorApiService
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

class ClarifaiRepositoryImpl(
    private val apiService: ClarifaiKtorApiService,
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
            
            val clarifaiResponse = apiService.recognizeFood(request)
            Result.success(clarifaiResponse).map { response ->
                clarifaiMapper.mapToFoodItems(response)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
