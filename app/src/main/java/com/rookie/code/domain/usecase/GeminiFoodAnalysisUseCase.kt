package com.rookie.code.domain.usecase

import com.rookie.code.domain.entities.GeminiFoodAnalysis
import com.rookie.code.domain.repository.GeminiFoodAnalysisRepository
import com.rookie.code.domain.service.ImageProcessingService
class GeminiFoodAnalysisUseCase(
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

