package com.bmqa.brac.fitnesstracker.data.repository

import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import com.bmqa.brac.fitnesstracker.domain.entities.TotalNutrition
import com.bmqa.brac.fitnesstracker.domain.repository.GeminiFoodAnalysisRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
class MockGeminiFoodAnalysisRepositoryImpl : GeminiFoodAnalysisRepository {
    
    override suspend fun analyzeFoodWithGemini(imageUri: String, base64Image: String): Result<GeminiFoodAnalysis> = withContext(Dispatchers.IO) {
        try {
            // Simulate network delay
            delay(2000)
            
            val mockFoodItems = listOf(
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
                        "May contribute to elevated cholesterol levels if consumed in excess",
                        "Risk of heterocyclic amines (HCAs) and polycyclic aromatic hydrocarbons (PAHs) formation during grilling"
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
                        "High glycemic index, may cause blood sugar spikes",
                        "Can be calorie-dense if consumed in large quantities"
                    ),
                    analysisSummary = "A healthy source of carbohydrates and essential nutrients."
                ),
                GeminiFoodItem(
                    name = "Mixed Green Salad with Tomato and Red Onion",
                    portion = "100g",
                    digestionTime = "1-2 hours",
                    healthStatus = HealthStatus.EXCELLENT,
                    calories = 50,
                    protein = "1g",
                    carbs = "5g",
                    fat = "3g",
                    healthBenefits = listOf(
                        "Rich in vitamins and minerals",
                        "Provides fiber for digestive health",
                        "Contains antioxidants to protect against cell damage"
                    ),
                    healthConcerns = listOf(
                        "Potential for pesticide residue if not organically grown"
                    ),
                    analysisSummary = "A nutritious addition to the meal, providing essential vitamins, minerals, and fiber."
                )
            )
            
            val mockTotalNutrition = TotalNutrition(
                name = "Pork Chop with Potatoes and Salad",
                totalPortion = "450g",
                totalCalories = 560,
                totalProtein = "34g",
                totalCarbs = "42g",
                totalFat = "28g",
                overallHealthStatus = HealthStatus.MODERATE
            )
            
            Result.success(
                GeminiFoodAnalysis(
                    isError = false,
                    errorMessage = "",
                    foodItems = mockFoodItems,
                    totalNutrition = mockTotalNutrition,
                    analysisSummary = "The meal provides a good balance of protein and carbohydrates. The high fat content from the pork chop is a concern, suggesting moderation in portion size. The salad adds essential vitamins and minerals. Overall, a moderately healthy meal."
                )
            )
        } catch (e: Exception) {
            Result.success(
                GeminiFoodAnalysis(
                    isError = true,
                    errorMessage = e.message ?: "Unknown error occurred",
                    foodItems = emptyList(),
                    totalNutrition = null,
                    analysisSummary = "Failed to analyze food"
                )
            )
        }
    }
}
