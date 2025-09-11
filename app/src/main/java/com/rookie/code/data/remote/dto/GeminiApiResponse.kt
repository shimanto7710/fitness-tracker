package com.rookie.code.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiApiResponse(
    @SerializedName("isError")
    val isError: Boolean = false,
    @SerializedName("errorMessage")
    val errorMessage: String = "",
    @SerializedName("foodItems")
    val foodItems: List<GeminiApiFoodItem>,
    @SerializedName("totalNutrition")
    val totalNutrition: GeminiApiTotalNutrition?,
    @SerializedName("analysisSummary")
    val analysisSummary: String
)

data class GeminiApiFoodItem(
    @SerializedName("name")
    val name: String,
    @SerializedName("portion")
    val portion: String,
    @SerializedName("digestionTime")
    val digestionTime: String,
    @SerializedName("healthStatus")
    val healthStatus: String,
    @SerializedName("calories")
    val calories: Int?,
    @SerializedName("protein")
    val protein: String?,
    @SerializedName("carbs")
    val carbs: String?,
    @SerializedName("fat")
    val fat: String?,
    @SerializedName("healthBenefits")
    val healthBenefits: List<String>,
    @SerializedName("healthConcerns")
    val healthConcerns: List<String>,
    @SerializedName("analysisSummary")
    val analysisSummary: String
)

data class GeminiApiTotalNutrition(
    @SerializedName("name")
    val name: String,
    @SerializedName("totalPortion")
    val totalPortion: String,
    @SerializedName("totalCalories")
    val totalCalories: Int?,
    @SerializedName("totalProtein")
    val totalProtein: String?,
    @SerializedName("totalCarbs")
    val totalCarbs: String?,
    @SerializedName("totalFat")
    val totalFat: String?,
    @SerializedName("overallHealthStatus")
    val overallHealthStatus: String
)
