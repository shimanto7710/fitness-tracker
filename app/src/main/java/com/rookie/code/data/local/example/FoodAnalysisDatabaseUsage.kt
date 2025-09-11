package com.rookie.code.data.local.example

import android.content.Context
import android.graphics.Bitmap
import com.rookie.code.data.local.repository.LocalFoodAnalysisRepository
import com.rookie.code.domain.entities.GeminiFoodAnalysis
import com.rookie.code.domain.entities.GeminiFoodItem
import com.rookie.code.domain.entities.HealthStatus
import com.rookie.code.domain.entities.TotalNutrition

/**
 * Example usage of the Room database for storing Gemini food analysis responses
 */
class FoodAnalysisDatabaseUsage(private val context: Context) {

    private val repository = LocalFoodAnalysisRepository(context)

    /**
     * Example: Save a complete food analysis with image
     */
    suspend fun saveExampleFoodAnalysis(imageBitmap: Bitmap, imageUri: String) {
        val foodAnalysis = GeminiFoodAnalysis(
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
                        "May contribute to elevated cholesterol levels if consumed in excess"
                    ),
                    analysisSummary = "A good source of protein but also high in fat. Moderation is key."
                ),
                GeminiFoodItem(
                    name = "Boiled Potatoes with Dill",
                    portion = "200g",
                    digestionTime = "2-3 hours",
                    healthStatus = HealthStatus.GOOD,
                    calories = 160,
                    protein = "3g",
                    carbs = "37g",
                    fat = "0g",
                    healthBenefits = listOf(
                        "Good source of potassium",
                        "Provides carbohydrates for energy",
                        "Contains vitamin C and B6"
                    ),
                    healthConcerns = listOf(
                        "High glycemic index, may cause blood sugar spikes"
                    ),
                    analysisSummary = "A healthy source of carbohydrates and essential nutrients."
                )
            ),
            totalNutrition = TotalNutrition(
                name = "Pork Chop with Potatoes and Salad",
                totalPortion = "450g",
                totalCalories = 560,
                totalProtein = "34g",
                totalCarbs = "42g",
                totalFat = "28g",
                overallHealthStatus = HealthStatus.MODERATE
            ),
            analysisSummary = "The meal provides a good balance of protein and carbohydrates. The high fat content from the pork chop is a concern, suggesting moderation in portion size.",
            dateNTime = "2024-10-05 12:46 PM"
        )

        // Save to database with image
        val analysisId = repository.saveFoodAnalysis(
            foodAnalysis = foodAnalysis,
            imageUri = imageUri,
            imageBitmap = imageBitmap
        )

        println("Food analysis saved with ID: $analysisId")
    }

    /**
     * Example: Get all food analyses
     */
    suspend fun getAllFoodAnalyses() {
        repository.getAllFoodAnalyses().collect { analyses ->
            analyses.forEach { analysis ->
                println("Analysis: ${analysis.totalNutrition?.name}")
                println("Date: ${analysis.dateNTime}")
                println("Total Calories: ${analysis.totalNutrition?.totalCalories}")
                println("---")
            }
        }
    }

    /**
     * Example: Get recent food analyses (for recently used list)
     */
    suspend fun getRecentFoodAnalyses() {
        repository.getRecentFoodAnalyses(limit = 5).collect { analyses ->
            analyses.forEach { analysis ->
                println("Recent: ${analysis.totalNutrition?.name}")
                println("Calories: ${analysis.totalNutrition?.totalCalories}")
            }
        }
    }

    /**
     * Example: Get specific food analysis by ID
     */
    suspend fun getFoodAnalysisById(id: Long) {
        val analysis = repository.getFoodAnalysisById(id)
        analysis?.let {
            println("Found analysis: ${it.totalNutrition?.name}")
            println("Food items: ${it.foodItems.size}")
        }
    }

    /**
     * Example: Delete a food analysis
     */
    suspend fun deleteFoodAnalysis(id: Long) {
        repository.deleteFoodAnalysis(id)
        println("Analysis with ID $id deleted")
    }
}

