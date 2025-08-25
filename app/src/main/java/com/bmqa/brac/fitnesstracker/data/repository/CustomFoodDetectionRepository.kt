package com.bmqa.brac.fitnesstracker.data.repository

import android.content.Context
import android.net.Uri
import com.bmqa.brac.fitnesstracker.data.remote.datasource.CustomFoodDetectionDataSource
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import javax.inject.Inject

interface CustomFoodDetectionRepository {
    suspend fun detectFoodFromImage(imageUri: Uri, context: Context): Result<List<FoodItem>>
}

class CustomFoodDetectionRepositoryImpl @Inject constructor(
    private val dataSource: CustomFoodDetectionDataSource
) : CustomFoodDetectionRepository {
    
    override suspend fun detectFoodFromImage(imageUri: Uri, context: Context): Result<List<FoodItem>> {
        return dataSource.detectFoodFromImage(imageUri, context)
    }
}
