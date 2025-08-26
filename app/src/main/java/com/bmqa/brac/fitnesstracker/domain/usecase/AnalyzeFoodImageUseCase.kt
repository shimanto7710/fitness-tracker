package com.bmqa.brac.fitnesstracker.domain.usecase

import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse
import com.bmqa.brac.fitnesstracker.domain.repository.OpenRouterRepository
import javax.inject.Inject

class AnalyzeFoodImageUseCase @Inject constructor(
    private val repository: OpenRouterRepository
) {
    
    suspend operator fun invoke(imageBase64: String, customText: String? = null): Result<OpenRouterResponse> {
        return repository.analyzeFoodImage(imageBase64, customText)
    }
}
