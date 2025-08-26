package com.bmqa.brac.fitnesstracker.data.repository

import android.content.Context
import android.net.Uri
import com.bmqa.brac.fitnesstracker.common.utils.ImageUtils
import com.bmqa.brac.fitnesstracker.data.remote.datasource.OpenRouterRemoteDataSource
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterContent
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterMessage
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse
import com.bmqa.brac.fitnesstracker.domain.repository.OpenRouterRepository
import javax.inject.Inject

class OpenRouterRepositoryImpl @Inject constructor(
    private val remoteDataSource: OpenRouterRemoteDataSource,
    private val context: Context
) : OpenRouterRepository {
    
    override suspend fun analyzeFoodImage(imageUri: Uri, customText: String?): Result<OpenRouterResponse> {
        return try {
            // Convert Uri to base64
            val base64Image = ImageUtils.convertImageToBase64(context, imageUri)
                ?: return Result.failure(Exception("Failed to convert image to base64"))
            
            val request = createRequest(base64Image, customText)
            remoteDataSource.analyzeFood(request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun createRequest(imageBase64: String, customText: String?): OpenRouterRequest {
        val defaultText = "You are an AI that analyzes images of food on a plate. Identify all food items on the plate and provide the following info for each item: food name, calories, portion in grams, and coordinates (x, y, width, height), also a message overall calory and healty or not. Output strictly in JSON format."
        
        val textContent = OpenRouterContent.TextContent(
            text = customText ?: defaultText
        )
        
        val imageContent = OpenRouterContent.ImageContent(
            image_base64 = imageBase64
        )
        
        val message = OpenRouterMessage(
            role = "user",
            content = listOf(textContent, imageContent)
        )
        
        return OpenRouterRequest(
            model = "qwen/qwen-2.5-vl-7b-instruct",
            messages = listOf(message)
        )
    }
}
