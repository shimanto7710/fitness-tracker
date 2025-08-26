package com.bmqa.brac.fitnesstracker.data.remote.datasource

import android.content.Context
import android.net.Uri
import com.bmqa.brac.fitnesstracker.data.remote.api.OpenRouterApiService
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface OpenRouterRemoteDataSource {
    suspend fun analyzeFood(
        imageUri: Uri,
        customText: String?,
        context: Context
    ): Result<OpenRouterResponse>
}

class OpenRouterRemoteDataSourceImpl @Inject constructor(
    private val apiService: OpenRouterApiService
) : OpenRouterRemoteDataSource {
    
    override suspend fun analyzeFood(
        imageUri: Uri,
        customText: String?,
        context: Context
    ): Result<OpenRouterResponse> {
        return try {
            // Convert Uri to base64 data URL for OpenRouter API
            val base64DataUrl = convertUriToBase64DataUrl(imageUri, context)
            
            // Default text for food analysis
            val defaultText = "You are an AI that analyzes images of food on a plate. Identify all food items on the plate and provide the following info for each item: food name, calories, portion in grams, and coordinates (x, y, width, height), also a message overall calory and healty or not. Output strictly in JSON format."
            val analysisText = customText ?: defaultText
            
            val response = apiService.analyzeFood(
                model = "qwen/qwen-2.5-vl-7b-instruct",
                role = "user",
                textType = "text",
                text = analysisText,
                imageType = "image_url",
                imageUrl = base64DataUrl
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun convertUriToBase64DataUrl(uri: Uri, context: Context): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes() ?: throw Exception("Failed to read image")
        val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
        
        // Determine MIME type
        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        
        return "data:$mimeType;base64,$base64"
    }
}
