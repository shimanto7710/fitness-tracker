package com.bmqa.brac.fitnesstracker.data.repository

import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
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
                    name = "Grilled Chicken Breast",
                    portion = "1 medium piece (150g)",
                    digestionTime = "2-3 hours",
                    healthStatus = HealthStatus.EXCELLENT,
                    calories = 231,
                    protein = "43g",
                    carbs = "0g",
                    fat = "5g",
                    healthBenefits = listOf(
                        "High-quality protein for muscle building",
                        "Low in saturated fat",
                        "Rich in essential amino acids"
                    ),
                    healthConcerns = listOf(
                        "May be dry if overcooked"
                    )
                ),
                GeminiFoodItem(
                    name = "Steamed Broccoli",
                    portion = "1 cup (91g)",
                    digestionTime = "1-2 hours",
                    healthStatus = HealthStatus.EXCELLENT,
                    calories = 31,
                    protein = "3g",
                    carbs = "6g",
                    fat = "0.4g",
                    healthBenefits = listOf(
                        "High in vitamin C and K",
                        "Rich in fiber",
                        "Contains antioxidants"
                    ),
                    healthConcerns = emptyList()
                ),
                GeminiFoodItem(
                    name = "Brown Rice",
                    portion = "1/2 cup cooked (98g)",
                    digestionTime = "2-3 hours",
                    healthStatus = HealthStatus.GOOD,
                    calories = 111,
                    protein = "2.6g",
                    carbs = "23g",
                    fat = "0.9g",
                    healthBenefits = listOf(
                        "Good source of complex carbohydrates",
                        "Contains fiber and B vitamins",
                        "Gluten-free"
                    ),
                    healthConcerns = listOf(
                        "High in carbohydrates"
                    )
                )
            )
            
            Result.success(
                GeminiFoodAnalysis(
                    isError = false,
                    errorMessage = "",
                    foodItems = mockFoodItems,
                    analysisSummary = "This is a well-balanced meal with lean protein, vegetables, and complex carbohydrates. The grilled chicken provides high-quality protein, broccoli adds essential vitamins and fiber, and brown rice offers sustained energy. This combination supports muscle health, provides essential nutrients, and maintains stable blood sugar levels."
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
