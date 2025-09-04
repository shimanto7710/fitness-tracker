package com.bmqa.brac.fitnesstracker.domain.usecase

import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.domain.repository.CustomFoodDetectionRepository
import com.bmqa.brac.fitnesstracker.domain.service.ImageProcessingService
import javax.inject.Inject

class CustomFoodDetectionUseCase @Inject constructor(
    private val repository: CustomFoodDetectionRepository,
    private val imageProcessingService: ImageProcessingService
) {
    
    suspend operator fun invoke(imageUri: String): Result<List<FoodItem>> {
        return try {
            val base64Result = imageProcessingService.convertImageToBase64(imageUri)
            base64Result.fold(
                onSuccess = { base64Image ->
                    repository.detectFoodFromImage(imageUri, base64Image)
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
