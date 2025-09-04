package com.bmqa.brac.fitnesstracker.data.repository

import com.bmqa.brac.fitnesstracker.data.remote.datasource.CustomFoodDetectionDataSource
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.domain.repository.CustomFoodDetectionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomFoodDetectionRepositoryImpl @Inject constructor(
    private val dataSource: CustomFoodDetectionDataSource
) : CustomFoodDetectionRepository {
    
    override suspend fun detectFoodFromImage(imageUri: String, base64Image: String): Result<List<FoodItem>> {
        return dataSource.detectFoodFromImage(imageUri, base64Image)
    }
}
