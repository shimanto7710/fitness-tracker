package com.bmqa.brac.fitnesstracker.domain.service

interface ImageProcessingService {
    suspend fun convertImageToBase64(imageUri: String): Result<String>
}
