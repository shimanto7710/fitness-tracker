package com.bmqa.brac.fitnesstracker.domain.repository

import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem

interface CustomFoodDetectionRepository {
    suspend fun detectFoodFromImage(imageUri: String, base64Image: String): Result<List<FoodItem>>
}
