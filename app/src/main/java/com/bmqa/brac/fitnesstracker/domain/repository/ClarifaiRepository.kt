package com.bmqa.brac.fitnesstracker.domain.repository

import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem

interface ClarifaiRepository {
    suspend fun recognizeFoodFromBase64(base64Image: String): Result<List<FoodItem>>
}
