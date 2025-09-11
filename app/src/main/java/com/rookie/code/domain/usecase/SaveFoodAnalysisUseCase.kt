package com.rookie.code.domain.usecase

import android.graphics.Bitmap
import com.rookie.code.domain.entities.GeminiFoodAnalysis
import com.rookie.code.domain.repository.FoodAnalysisRepository


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
