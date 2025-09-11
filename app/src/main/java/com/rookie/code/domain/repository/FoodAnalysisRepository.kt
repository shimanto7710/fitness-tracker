package com.rookie.code.domain.repository

import android.graphics.Bitmap
import com.rookie.code.domain.entities.GeminiFoodAnalysis

/**
 * Repository interface for food analysis data persistence operations.
 * 
 * This interface defines the contract for persisting food analysis data
 * in the domain layer, following clean architecture principles.
 */
interface FoodAnalysisRepository {
    
    /**
     * Saves a food analysis result to local storage.
     * 
     * @param foodAnalysis The analysis result to save
     * @param imageUri The original image URI
     * @param imageBitmap The bitmap representation of the image
     */
    suspend fun saveFoodAnalysis(
        foodAnalysis: GeminiFoodAnalysis,
        imageUri: String,
        imageBitmap: Bitmap?
    )
}
