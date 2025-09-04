package com.bmqa.brac.fitnesstracker.data.service

import android.content.Context
import android.net.Uri
import com.bmqa.brac.fitnesstracker.domain.service.ImageProcessingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageProcessingServiceImpl @Inject constructor(
    private val context: Context
) : ImageProcessingService {
    
    override suspend fun convertImageToBase64(imageUri: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val uri = Uri.parse(imageUri)
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                return@withContext Result.failure(Exception("Unable to open input stream for URI: $imageUri"))
            }
            
            val bytes = inputStream.readBytes()
            val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
            
            Result.success(base64)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
