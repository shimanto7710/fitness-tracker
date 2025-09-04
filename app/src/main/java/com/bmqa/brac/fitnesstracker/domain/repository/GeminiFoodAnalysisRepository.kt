package com.bmqa.brac.fitnesstracker.domain.repository

import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis

interface GeminiFoodAnalysisRepository {
    suspend fun analyzeFoodWithGemini(imageUri: String, base64Image: String): Result<GeminiFoodAnalysis>
}
