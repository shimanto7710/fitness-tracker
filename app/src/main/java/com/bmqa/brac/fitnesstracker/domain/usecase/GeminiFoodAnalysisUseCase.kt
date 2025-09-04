package com.bmqa.brac.fitnesstracker.domain.usecase

import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.repository.GeminiFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.service.ImageProcessingService
import javax.inject.Inject

class GeminiFoodAnalysisUseCase @Inject constructor(
    private val repository: GeminiFoodAnalysisRepository,
    private val imageProcessingService: ImageProcessingService
) {
    
    suspend operator fun invoke(imageUri: String): Result<GeminiFoodAnalysis> {
        return try {
            val base64Result = imageProcessingService.convertImageToBase64(imageUri)
            base64Result.fold(
                onSuccess = { base64Image ->
                    repository.analyzeFoodWithGemini(imageUri, base64Image)
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

