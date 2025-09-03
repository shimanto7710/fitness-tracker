package com.bmqa.brac.fitnesstracker.domain.usecase

import android.content.Context
import android.net.Uri
import com.bmqa.brac.fitnesstracker.data.repository.GeminiFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import javax.inject.Inject

class GeminiFoodAnalysisUseCase @Inject constructor(
    private val repository: GeminiFoodAnalysisRepository
) {
    
    suspend operator fun invoke(imageUri: Uri, context: Context): Result<GeminiFoodAnalysis> {
        return repository.analyzeFoodWithGemini(imageUri, context)
    }
}

