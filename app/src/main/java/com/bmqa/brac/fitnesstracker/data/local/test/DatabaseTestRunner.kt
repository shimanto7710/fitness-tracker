package com.bmqa.brac.fitnesstracker.data.local.test

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import com.bmqa.brac.fitnesstracker.data.local.repository.LocalFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import com.bmqa.brac.fitnesstracker.domain.entities.TotalNutrition
import kotlinx.coroutines.runBlocking

/**
 * Simple test runner for the Room database
 * You can call this from anywhere in your app to test database functionality
 */
class DatabaseTestRunner(private val context: Context) {

    fun runAllTests() {
        println("🧪 Starting Database Tests...")
        
        runBlocking {
            val repository = LocalFoodAnalysisRepository(context)
            
            try {
                // Test 1: Save analysis
                println("📝 Test 1: Saving food analysis...")
                val testAnalysis = createTestAnalysis()
                val testBitmap = createTestBitmap()
                
                val analysisId = repository.saveFoodAnalysisWithId(
                    foodAnalysis = testAnalysis,
                    imageUri = "test://image/uri",
                    imageBitmap = testBitmap
                )
                println("✅ Analysis saved with ID: $analysisId")
                
                // Test 2: Retrieve analysis
                println("📖 Test 2: Retrieving analysis...")
                val retrievedAnalysis = repository.getFoodAnalysisById(analysisId)
                if (retrievedAnalysis != null) {
                    println("✅ Analysis retrieved: ${retrievedAnalysis.totalNutrition?.name}")
                    println("   - Food items: ${retrievedAnalysis.foodItems.size}")
                    println("   - Total calories: ${retrievedAnalysis.totalNutrition?.totalCalories}")
                } else {
                    println("❌ Failed to retrieve analysis")
                }
                
                // Test 3: Get all analyses
                println("📋 Test 3: Getting all analyses...")
                repository.getAllFoodAnalyses().collect { analyses ->
                    println("✅ Found ${analyses.size} total analyses")
                    analyses.forEach { analysis ->
                        println("   - ${analysis.totalNutrition?.name}: ${analysis.totalNutrition?.totalCalories} cal")
                    }
                }
                
                // Test 4: Get recent analyses
                println("🕒 Test 4: Getting recent analyses...")
                repository.getRecentFoodAnalyses(limit = 3).collect { recentAnalyses ->
                    println("✅ Found ${recentAnalyses.size} recent analyses")
                }
                
                // Test 5: Delete analysis
                println("🗑️ Test 5: Deleting analysis...")
                repository.deleteFoodAnalysis(analysisId)
                val deletedAnalysis = repository.getFoodAnalysisById(analysisId)
                if (deletedAnalysis == null) {
                    println("✅ Analysis successfully deleted")
                } else {
                    println("❌ Failed to delete analysis")
                }
                
                println("🎉 All tests completed successfully!")
                
            } catch (e: Exception) {
                println("❌ Test failed with error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun createTestAnalysis(): GeminiFoodAnalysis {
        return GeminiFoodAnalysis(
            isError = false,
            errorMessage = "",
            foodItems = listOf(
                GeminiFoodItem(
                    name = "Test Grilled Salmon",
                    portion = "120g",
                    digestionTime = "3-4 hours",
                    healthStatus = HealthStatus.EXCELLENT,
                    calories = 200,
                    protein = "22g",
                    carbs = "0g",
                    fat = "12g",
                    healthBenefits = listOf(
                        "High in omega-3 fatty acids",
                        "Excellent source of protein",
                        "Supports heart health"
                    ),
                    healthConcerns = listOf("None"),
                    analysisSummary = "Excellent source of lean protein and healthy fats"
                ),
                GeminiFoodItem(
                    name = "Test Quinoa Salad",
                    portion = "150g",
                    digestionTime = "2-3 hours",
                    healthStatus = HealthStatus.EXCELLENT,
                    calories = 180,
                    protein = "6g",
                    carbs = "32g",
                    fat = "3g",
                    healthBenefits = listOf(
                        "Complete protein source",
                        "High in fiber",
                        "Rich in minerals"
                    ),
                    healthConcerns = listOf("None"),
                    analysisSummary = "Nutritious grain with complete amino acid profile"
                )
            ),
            totalNutrition = TotalNutrition(
                name = "Test Salmon & Quinoa Bowl",
                totalPortion = "270g",
                totalCalories = 380,
                totalProtein = "28g",
                totalCarbs = "32g",
                totalFat = "15g",
                overallHealthStatus = HealthStatus.EXCELLENT
            ),
            analysisSummary = "A perfectly balanced meal with excellent protein, healthy fats, and complex carbohydrates. Ideal for post-workout recovery and overall health.",
            dateNTime = "2024-01-15 18:30 PM"
        )
    }

    private fun createTestBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.GREEN)
        return bitmap
    }
}


