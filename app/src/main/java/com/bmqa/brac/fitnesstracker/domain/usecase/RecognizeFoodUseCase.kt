package com.bmqa.brac.fitnesstracker.domain.usecase

import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.domain.repository.ClarifaiRepository
class RecognizeFoodUseCase(
    private val clarifaiRepository: ClarifaiRepository
) {
    suspend operator fun invoke(base64Image: String): Result<List<FoodItem>> {
        return clarifaiRepository.recognizeFoodFromBase64(base64Image)
    }
}
