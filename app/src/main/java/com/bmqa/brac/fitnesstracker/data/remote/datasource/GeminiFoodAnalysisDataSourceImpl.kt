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
                // TEMPORARY: Return mock data instead of calling API
                val mockData = createMockFoodAnalysis()
                Result.success(mockData)
                
                /* REAL API CALL - COMMENTED OUT FOR TEMPORARY MOCK
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
                */

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
    
    // TEMPORARY: Mock data function for testing
    private fun createMockFoodAnalysis(): GeminiFoodAnalysis {
        return GeminiFoodAnalysis(
            isError = false,
            errorMessage = "",
            foodItems = listOf(
                GeminiFoodItem(
                    name = "Grilled Pork Chop",
                    portion = "150g",
                    digestionTime = "3-4 hours",
                    healthStatus = HealthStatus.MODERATE,
                    calories = 350,
                    protein = "30g",
                    carbs = "0g",
                    fat = "25g",
                    healthBenefits = listOf(
                        "Good source of protein",
                        "Provides essential amino acids"
                    ),
                    healthConcerns = listOf(
                        "High in saturated fat",
                        "May contribute to high cholesterol if consumed in excess"
                    )
                ),
                GeminiFoodItem(
                    name = "Boiled Potatoes with Dill",
                    portion = "3 medium potatoes",
                    digestionTime = "2-3 hours",
                    healthStatus = HealthStatus.GOOD,
                    calories = 210,
                    protein = "5g",
                    carbs = "45g",
                    fat = "0g",
                    healthBenefits = listOf(
                        "Good source of potassium",
                        "Provides complex carbohydrates for energy"
                    ),
                    healthConcerns = listOf(
                        "High glycemic index, may cause blood sugar spikes"
                    )
                ),
                GeminiFoodItem(
                    name = "Tomato and Onion Salad with Greens",
                    portion = "1 cup",
                    digestionTime = "1-2 hours",
                    healthStatus = HealthStatus.EXCELLENT,
                    calories = 50,
                    protein = "2g",
                    carbs = "8g",
                    fat = "2g",
                    healthBenefits = listOf(
                        "Rich in vitamins and antioxidants",
                        "Provides dietary fiber for digestive health"
                    ),
                    healthConcerns = emptyList()
                )
            ),
            analysisSummary = "This meal provides a good source of protein and carbohydrates, along with essential vitamins and minerals from the salad. However, the pork chop is high in saturated fat, so moderation is key. The potatoes have a high glycemic index which is a concern for people with diabetes. The meal is relatively balanced, offering both nutritional benefits and potential drawbacks depending on individual health needs and dietary habits."
        )
    }
}
