package com.bmqa.brac.foodlens.domain.usecase

import android.graphics.Bitmap
import com.bmqa.brac.foodlens.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.foodlens.domain.repository.FoodAnalysisRepository


class SaveFoodAnalysisUseCase(
    private val foodAnalysisRepository: FoodAnalysisRepository
) {

    suspend operator fun invoke(
        foodAnalysis: GeminiFoodAnalysis,
        imageUri: String,
        imageBitmap: Bitmap?
    ): Result<Unit> {
        return try {
            foodAnalysisRepository.saveFoodAnalysis(
                foodAnalysis = foodAnalysis,
                imageUri = imageUri,
                imageBitmap = imageBitmap
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
