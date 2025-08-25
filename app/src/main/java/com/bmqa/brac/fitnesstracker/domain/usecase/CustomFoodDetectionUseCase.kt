package com.bmqa.brac.fitnesstracker.domain.usecase

import android.content.Context
import android.net.Uri
import com.bmqa.brac.fitnesstracker.data.repository.CustomFoodDetectionRepository
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import javax.inject.Inject

class CustomFoodDetectionUseCase @Inject constructor(
    private val repository: CustomFoodDetectionRepository
) {
    
    suspend operator fun invoke(imageUri: Uri, context: Context): Result<List<FoodItem>> {
        return repository.detectFoodFromImage(imageUri, context)
    }
}
