package com.bmqa.brac.foodlens.domain.service

interface ImageProcessingService {
    suspend fun convertImageToBase64(imageUri: String): Result<String>
}
