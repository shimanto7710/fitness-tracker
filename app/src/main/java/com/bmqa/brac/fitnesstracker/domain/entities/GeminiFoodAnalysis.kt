package com.bmqa.brac.fitnesstracker.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class GeminiFoodAnalysis(
    val id: Long = 0,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val foodItems: List<GeminiFoodItem>,
    val totalNutrition: TotalNutrition?,
    val analysisSummary: String,
    val dateNTime: String?=null,
    val imageUri: String? = null,
    val imagePath: String? = null,
    val base64Image: String? = null,
    val selectedDate: String? = null
)

@Serializable
data class GeminiFoodItem(
    val name: String,
    val portion: String,
    val digestionTime: String,
    val healthStatus: HealthStatus,
    val calories: Int?,
    val protein: String?,
    val carbs: String?,
    val fat: String?,
    val healthBenefits: List<String>,
    val healthConcerns: List<String>,
    val analysisSummary: String
)

@Serializable
data class TotalNutrition(
    val name: String,
    val totalPortion: String,
    val totalCalories: Int?,
    val totalProtein: String?,
    val totalCarbs: String?,
    val totalFat: String?,
    val overallHealthStatus: HealthStatus
)

@Serializable
enum class HealthStatus {
    EXCELLENT,
    GOOD,
    MODERATE,
    POOR,
    UNKNOWN
}

@Serializable
data class GeminiAnalysisRequest(
    val imageUri: String,
    val prompt: String
)

