package com.rookie.code.domain.repository

import com.rookie.code.domain.entities.GeminiFoodAnalysis

interface GeminiFoodAnalysisRepository {
    suspend fun analyzeFoodWithGemini(imageUri: String, base64Image: String): Result<GeminiFoodAnalysis>
}
