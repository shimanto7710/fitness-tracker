package com.bmqa.brac.fitnesstracker.data.remote.datasource

import android.content.Context
import android.net.Uri
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import javax.inject.Inject

interface GeminiFoodAnalysisDataSource {
    suspend fun analyzeFoodWithGemini(imageUri: String, base64Image: String): Result<GeminiFoodAnalysis>
}
