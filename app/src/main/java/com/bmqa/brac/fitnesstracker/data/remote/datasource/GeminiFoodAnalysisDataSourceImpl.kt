package com.bmqa.brac.fitnesstracker.data.remote.datasource

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.bmqa.brac.fitnesstracker.common.constants.GeminiConstants
import com.bmqa.brac.fitnesstracker.data.remote.dto.GeminiApiResponse
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiFoodAnalysisDataSourceImpl @Inject constructor() : GeminiFoodAnalysisDataSource {
    
    private val generativeModel = GenerativeModel(
        modelName = GeminiConstants.MODEL_NAME,
        apiKey = GeminiConstants.API_KEY
    )
    
    private val gson = Gson()
    
    override suspend fun analyzeFoodWithGemini(imageUri: Uri, context: Context): Result<GeminiFoodAnalysis> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream = context.contentResolver.openInputStream(imageUri)
                    ?: return@withContext Result.failure(Exception("Could not open image stream"))
                
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                    ?: return@withContext Result.failure(Exception("Could not decode image"))
                
                val prompt = GeminiConstants.FOOD_ANALYSIS_PROMPT
                
                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )
                
                val responseText = response.text ?: return@withContext Result.failure(Exception("Empty response from Gemini"))
                
                // Parse the JSON response
                val foodAnalysis = parseGeminiResponse(responseText)
                Result.success(foodAnalysis)
                
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    private fun parseGeminiResponse(responseText: String): GeminiFoodAnalysis {
        return try {
            // First, try to parse using the proper model
            val apiResponse: GeminiApiResponse = gson.fromJson(responseText, GeminiApiResponse::class.java)
            
            // Convert API response to domain entities
            val foodItems = apiResponse.foodItems.map { apiItem ->
                GeminiFoodItem(
                    name = apiItem.name,
                    portion = apiItem.portion,
                    digestionTime = apiItem.digestionTime,
                    healthStatus = parseHealthStatus(apiItem.healthStatus),
                    calories = apiItem.calories,
                    protein = apiItem.protein,
                    carbs = apiItem.carbs,
                    fat = apiItem.fat,
                    healthBenefits = apiItem.healthBenefits,
                    healthConcerns = apiItem.healthConcerns
                )
            }
            
            GeminiFoodAnalysis(
                isError = apiResponse.isError,
                errorMessage = apiResponse.errorMessage,
                foodItems = foodItems,
                analysisSummary = apiResponse.analysisSummary
            )
            
        } catch (e: Exception) {
            // If direct parsing fails, try manual JSON parsing as fallback
            try {
                val jsonParser = JsonParser()
                val jsonElement = jsonParser.parse(responseText)
                
                if (jsonElement.isJsonObject) {
                    val jsonObject = jsonElement.asJsonObject
                    
                    // Extract analysis summary
                    val analysisSummary = jsonObject.get("analysisSummary")?.asString 
                        ?: "Food analysis completed successfully."
                    
                    // Extract food items
                    val foodItemsArray = jsonObject.getAsJsonArray("foodItems")
                    val foodItems = mutableListOf<GeminiFoodItem>()
                    
                    if (foodItemsArray != null) {
                        for (item in foodItemsArray) {
                            if (item.isJsonObject) {
                                val itemObj = item.asJsonObject
                                val foodItem = GeminiFoodItem(
                                    name = itemObj.get("name")?.asString ?: "Unknown Food",
                                    portion = itemObj.get("portion")?.asString ?: "1 serving",
                                    digestionTime = itemObj.get("digestionTime")?.asString ?: "2-3 hours",
                                    healthStatus = parseHealthStatus(itemObj.get("healthStatus")?.asString),
                                    calories = itemObj.get("calories")?.asInt,
                                    protein = itemObj.get("protein")?.asString,
                                    carbs = itemObj.get("carbs")?.asString,
                                    fat = itemObj.get("fat")?.asString,
                                    healthBenefits = parseStringList(itemObj.getAsJsonArray("healthBenefits")),
                                    healthConcerns = parseStringList(itemObj.getAsJsonArray("healthConcerns"))
                                )
                                foodItems.add(foodItem)
                            }
                        }
                    }
                    
                    // If no food items were parsed, create a fallback
                    if (foodItems.isEmpty()) {
                        foodItems.add(
                            GeminiFoodItem(
                                name = "Detected Food",
                                portion = "1 serving",
                                digestionTime = "2-3 hours",
                                healthStatus = HealthStatus.UNKNOWN,
                                calories = null,
                                protein = null,
                                carbs = null,
                                fat = null,
                                healthBenefits = emptyList(),
                                healthConcerns = emptyList()
                            )
                        )
                    }
                    
                    GeminiFoodAnalysis(
                        isError = false,
                        errorMessage = "",
                        foodItems = foodItems,
                        analysisSummary = analysisSummary
                    )
                } else {
                    // If response is not JSON, treat it as plain text analysis
                    createFallbackAnalysis(responseText)
                }
            } catch (e2: Exception) {
                // If all parsing fails, create a fallback analysis
                createFallbackAnalysis(responseText)
            }
        }
    }
    
    private fun parseHealthStatus(statusString: String?): HealthStatus {
        return when (statusString?.uppercase()) {
            "EXCELLENT" -> HealthStatus.EXCELLENT
            "GOOD" -> HealthStatus.GOOD
            "MODERATE" -> HealthStatus.MODERATE
            "POOR" -> HealthStatus.POOR
            else -> HealthStatus.UNKNOWN
        }
    }
    
    private fun parseStringList(jsonArray: com.google.gson.JsonArray?): List<String> {
        if (jsonArray == null) return emptyList()
        return jsonArray.mapNotNull { it.asString }
    }
    
    private fun createFallbackAnalysis(responseText: String): GeminiFoodAnalysis {
        return GeminiFoodAnalysis(
            isError = false,
            errorMessage = "",
            foodItems = listOf(
                GeminiFoodItem(
                    name = "Analyzed Food",
                    portion = "1 serving",
                    digestionTime = "2-3 hours",
                    healthStatus = HealthStatus.UNKNOWN,
                    calories = null,
                    protein = null,
                    carbs = null,
                    fat = null,
                    healthBenefits = emptyList(),
                    healthConcerns = emptyList()
                )
            ),
            analysisSummary = responseText.takeIf { it.isNotBlank() } 
                ?: "Food analysis completed. Please check the image for details."
        )
    }
}
