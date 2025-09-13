package com.rookie.code.domain.repository

import android.graphics.Bitmap
import com.rookie.code.domain.entities.GeminiFoodAnalysis

interface FoodAnalysisRepository {

    suspend fun saveFoodAnalysis(
        foodAnalysis: GeminiFoodAnalysis,
        imageUri: String,
        imageBitmap: Bitmap?
    )
}
