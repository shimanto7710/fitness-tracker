package com.bmqa.brac.fitnesstracker.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.bmqa.brac.fitnesstracker.common.constants.GeminiConstants
import com.bmqa.brac.fitnesstracker.data.remote.dto.GeminiApiResponse
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import com.bmqa.brac.fitnesstracker.domain.repository.GeminiFoodAnalysisRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
class GeminiFoodAnalysisRepositoryImpl(
    private val context: Context
) : GeminiFoodAnalysisRepository {
    
    override suspend fun analyzeFoodWithGemini(imageUri: String, base64Image: String): Result<GeminiFoodAnalysis> = withContext(Dispatchers.IO) {
        try {
            val generativeModel = GenerativeModel(
                modelName = GeminiConstants.MODEL_NAME,
                apiKey = GeminiConstants.API_KEY
            )

            val content = content {
                image(loadImageFromBase64(base64Image))
                text(GeminiConstants.FOOD_ANALYSIS_PROMPT)
            }
            
            val response = generativeModel.generateContent(content)
            val responseText = response.text ?: ""
            
            val gson = Gson()
            val jsonParser = JsonParser()
            val jsonElement = jsonParser.parse(responseText)
            val geminiResponse = gson.fromJson(jsonElement, GeminiApiResponse::class.java)
            
            val foodItems = geminiResponse.foodItems?.map { item ->
                GeminiFoodItem(
                    name = item.name ?: "Unknown",
                    portion = item.portion ?: "1 serving",
                    digestionTime = item.digestionTime ?: "Unknown",
                    healthStatus = when (item.healthStatus?.uppercase()) {
                        "EXCELLENT" -> HealthStatus.EXCELLENT
                        "GOOD" -> HealthStatus.GOOD
                        "MODERATE" -> HealthStatus.MODERATE
                        "POOR" -> HealthStatus.POOR
                        else -> HealthStatus.UNKNOWN
                    },
                    calories = item.calories,
                    protein = item.protein,
                    carbs = item.carbs,
                    fat = item.fat,
                    healthBenefits = item.healthBenefits ?: emptyList(),
                    healthConcerns = item.healthConcerns ?: emptyList()
                )
            } ?: emptyList()
            
            Result.success(
                GeminiFoodAnalysis(
                    isError = false,
                    errorMessage = "",
                    foodItems = foodItems,
                    analysisSummary = geminiResponse.analysisSummary ?: "Analysis completed"
                )
            )
        } catch (e: Exception) {
            Result.success(
                GeminiFoodAnalysis(
                    isError = true,
                    errorMessage = e.message ?: "Unknown error occurred",
                    foodItems = emptyList(),
                    analysisSummary = "Failed to analyze food"
                )
            )
        }
    }
    
    private fun loadImageFromBase64(base64String: String): Bitmap {
        val imageBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}

