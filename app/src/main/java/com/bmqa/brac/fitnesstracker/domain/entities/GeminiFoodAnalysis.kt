package com.bmqa.brac.fitnesstracker.domain.entities

data class GeminiFoodAnalysis(
    val isError: Boolean = false,
    val errorMessage: String = "",
    val foodItems: List<GeminiFoodItem>,
    val totalNutrition: TotalNutrition?,
    val analysisSummary: String,
    val dateNTime: String?=null
)

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

data class TotalNutrition(
    val name: String,
    val totalPortion: String,
    val totalCalories: Int?,
    val totalProtein: String?,
    val totalCarbs: String?,
    val totalFat: String?,
    val overallHealthStatus: HealthStatus
)

enum class HealthStatus {
    EXCELLENT,
    GOOD,
    MODERATE,
    POOR,
    UNKNOWN
}

data class GeminiAnalysisRequest(
    val imageUri: String,
    val prompt: String
)

