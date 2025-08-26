package com.bmqa.brac.fitnesstracker.domain.usecase

import android.net.Uri
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse
import com.bmqa.brac.fitnesstracker.domain.repository.OpenRouterRepository
import javax.inject.Inject

class AnalyzeFoodImageUseCase @Inject constructor(
    private val repository: OpenRouterRepository
) {
    
    suspend operator fun invoke(imageUri: Uri, customText: String? = null): Result<OpenRouterResponse> {
        return repository.analyzeFoodImage(imageUri, customText)
    }
}
