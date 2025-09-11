package com.rookie.code.domain.service

interface ImageProcessingService {
    suspend fun convertImageToBase64(imageUri: String): Result<String>
}
