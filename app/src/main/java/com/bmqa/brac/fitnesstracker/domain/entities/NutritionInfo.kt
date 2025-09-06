package com.bmqa.brac.fitnesstracker.domain.entities

/**
 * Data class representing nutrition information for a meal
 */
data class NutritionInfo(
    val calories: Int,
    val protein: String,
    val carbs: String,
    val fats: String,
    val healthScore: Int, // 1-10 scale
    val ingredients: List<String> = emptyList(),
    val mealName: String = "",
    val servingSize: String = "1",
    val timestamp: String = ""
)

/**
 * Data class representing a meal with image and nutrition details
 */
data class Meal(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val nutritionInfo: NutritionInfo,
    val timestamp: Long = System.currentTimeMillis()
)
