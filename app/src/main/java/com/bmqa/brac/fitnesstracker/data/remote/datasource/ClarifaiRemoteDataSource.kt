package com.bmqa.brac.fitnesstracker.data.remote.datasource

import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiResponse

/**
 * Remote data source interface for Clarifai API
 * Following Clean Architecture principles - defines contract for remote data operations
 */
interface ClarifaiRemoteDataSource {
    
    /**
     * Recognizes food from base64 encoded image
     * @param base64Image Base64 encoded image string
     * @return Result containing ClarifaiResponse or error
     */
    suspend fun recognizeFood(base64Image: String): Result<ClarifaiResponse>
}
