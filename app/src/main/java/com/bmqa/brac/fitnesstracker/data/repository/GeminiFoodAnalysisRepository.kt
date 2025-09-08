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
import com.bmqa.brac.fitnesstracker.domain.entities.TotalNutrition
import com.bmqa.brac.fitnesstracker.domain.repository.GeminiFoodAnalysisRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
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
            
            // Clean the response text to extract only JSON
            val cleanedJson = cleanJsonResponse(responseText)
            
            // Log the cleaned JSON for debugging
            android.util.Log.d("GeminiRepository", "Cleaned JSON: $cleanedJson")
            
            val gson = Gson()
            val geminiResponse = try {
                // Try parsing with lenient mode first
                val jsonReader = JsonReader(java.io.StringReader(cleanedJson))
                jsonReader.isLenient = true
                gson.fromJson(jsonReader, GeminiApiResponse::class.java)
            } catch (e: Exception) {
                // If lenient parsing fails, try with regular parsing
                android.util.Log.w("GeminiRepository", "Lenient parsing failed, trying regular parsing: ${e.message}")
                gson.fromJson(cleanedJson, GeminiApiResponse::class.java)
            }
            
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
                    healthConcerns = item.healthConcerns ?: emptyList(),
                    analysisSummary = item.analysisSummary ?: "No analysis available"
                )
            } ?: emptyList()
            
            val totalNutrition = geminiResponse.totalNutrition?.let { total ->
                TotalNutrition(
                    name = total.name ?: "Mixed Meal",
                    totalPortion = total.totalPortion ?: "Unknown",
                    totalCalories = total.totalCalories,
                    totalProtein = total.totalProtein,
                    totalCarbs = total.totalCarbs,
                    totalFat = total.totalFat,
                    overallHealthStatus = when (total.overallHealthStatus?.uppercase()) {
                        "EXCELLENT" -> HealthStatus.EXCELLENT
                        "GOOD" -> HealthStatus.GOOD
                        "MODERATE" -> HealthStatus.MODERATE
                        "POOR" -> HealthStatus.POOR
                        else -> HealthStatus.UNKNOWN
                    }
                )
            }
            
            Result.success(
                GeminiFoodAnalysis(
                    isError = false,
                    errorMessage = "",
                    foodItems = foodItems,
                    totalNutrition = totalNutrition,
                    analysisSummary = geminiResponse.analysisSummary ?: "Analysis completed"
                )
            )
        } catch (e: Exception) {
            android.util.Log.e("GeminiRepository", "Error analyzing food: ${e.message}", e)
            Result.success(
                GeminiFoodAnalysis(
                    isError = true,
                    errorMessage = when (e) {
                        is com.google.gson.stream.MalformedJsonException -> "Invalid JSON response from AI. Please try again."
                        is com.google.gson.JsonSyntaxException -> "Failed to parse AI response. Please try again."
                        else -> e.message ?: "Unknown error occurred"
                    },
                    foodItems = emptyList(),
                    totalNutrition = null,
                    analysisSummary = "Failed to analyze food"
                )
            )
        }
    }
    
    private fun loadImageFromBase64(base64String: String): Bitmap {
        val imageBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
    
    private fun cleanJsonResponse(responseText: String): String {
        // Remove any text before the first '{' and after the last '}'
        val startIndex = responseText.indexOf('{')
        val lastIndex = responseText.lastIndexOf('}')
        
        return if (startIndex != -1 && lastIndex != -1 && lastIndex > startIndex) {
            val jsonString = responseText.substring(startIndex, lastIndex + 1)
            
            // Additional cleaning: remove any markdown code blocks
            jsonString
                .replace("```json", "")
                .replace("```", "")
                .trim()
        } else {
            // If no JSON structure found, return the original text
            responseText
        }
    }
}

