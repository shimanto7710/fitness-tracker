package com.bmqa.brac.fitnesstracker.data.remote.datasource

import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.data.remote.api.ClarifaiApiService
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiResponse
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiUserAppId
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiInputRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiInputDataRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiImageRequest
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ClarifaiRemoteDataSource
 * Handles the actual API calls to Clarifai service
 */
@Singleton
class ClarifaiRemoteDataSourceImpl @Inject constructor(
    private val apiService: ClarifaiApiService
) : ClarifaiRemoteDataSource {
    
    override suspend fun recognizeFood(base64Image: String): Result<ClarifaiResponse> {
        return try {
            val request = createClarifaiRequest(base64Image)
            val response = apiService.recognizeFood(AppConstants.Api.AUTHORIZATION_HEADER, request)
            
            if (response.isSuccessful) {
                val clarifaiResponse = response.body()
                if (clarifaiResponse != null) {
                    Result.success(clarifaiResponse)
                } else {
                    Result.failure(Exception(AppConstants.ErrorMessages.EMPTY_RESPONSE))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: AppConstants.ErrorMessages.UNKNOWN_ERROR
                Result.failure(Exception("${AppConstants.ErrorMessages.API_ERROR}: ${response.code()} - $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun createClarifaiRequest(base64Image: String) = ClarifaiRequest(
        user_app_id = ClarifaiUserAppId(
            AppConstants.Api.USER_ID, 
            AppConstants.Api.APP_ID
        ),
        inputs = listOf(
            ClarifaiInputRequest(
                data = ClarifaiInputDataRequest(
                    image = ClarifaiImageRequest(base64 = base64Image)
                )
            )
        )
    )
}
