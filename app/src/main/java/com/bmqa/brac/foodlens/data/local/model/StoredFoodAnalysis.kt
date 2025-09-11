package com.bmqa.brac.foodlens.data.local.model

import android.graphics.Bitmap
import com.bmqa.brac.foodlens.domain.entities.GeminiFoodAnalysis

data class StoredFoodAnalysis(
    val id: Long,
    val foodAnalysis: GeminiFoodAnalysis,
    val imagePath: String? = null,
    val imageUri: String? = null,
    val imageBitmap: Bitmap? = null,
    val createdAt: Long
)

