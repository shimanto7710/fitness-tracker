package com.bmqa.brac.foodlens.domain.repository

import com.bmqa.brac.foodlens.domain.entities.GeminiFoodAnalysis

interface GeminiFoodAnalysisRepository {
    suspend fun analyzeFoodWithGemini(imageUri: String, base64Image: String): Result<GeminiFoodAnalysis>
}
