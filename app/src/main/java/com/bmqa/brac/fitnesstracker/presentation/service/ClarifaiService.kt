package com.bmqa.brac.fitnesstracker.presentation.service

import android.net.Uri
import android.util.Log
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.domain.usecase.RecognizeFoodUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClarifaiService @Inject constructor(
    private val recognizeFoodUseCase: RecognizeFoodUseCase
) {
    
    suspend fun recognizeFoodFromImage(imageUri: Uri, context: android.content.Context): List<FoodItem> = withContext(Dispatchers.IO) {
        try {
            // Convert URI to base64
            val base64Image = uriToBase64(imageUri, context)
            if (base64Image != null) {
                Log.d("ClarifaiService", "Image converted to base64, size: ${base64Image.length} characters")
                
                val result = recognizeFoodUseCase(base64Image)
                result.fold(
                    onSuccess = { foodItems ->
                        Log.d("ClarifaiService", "Successfully recognized ${foodItems.size} food items")
                        foodItems
                    },
                    onFailure = { exception ->
                        Log.e("ClarifaiService", "Food recognition failed", exception)
                        emptyList()
                    }
                )
            } else {
                Log.e("ClarifaiService", "Failed to convert image to base64")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ClarifaiService", "Error processing image", e)
            emptyList()
        }
    }
    
    suspend fun recognizeFoodFromBase64(base64Image: String): List<FoodItem> = withContext(Dispatchers.IO) {
        try {
            val result = recognizeFoodUseCase(base64Image)
            result.fold(
                onSuccess = { foodItems ->
                    Log.d("ClarifaiService", "Successfully recognized ${foodItems.size} food items")
                    foodItems
                },
                onFailure = { exception ->
                    Log.e("ClarifaiService", "Food recognition failed", exception)
                    emptyList()
                }
            )
        } catch (e: Exception) {
            Log.e("ClarifaiService", "Error processing base64 image", e)
            emptyList()
        }
    }
    
    private fun uriToBase64(uri: Uri, context: android.content.Context): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e("ClarifaiService", "Unable to open input stream for URI: $uri")
                return null
            }
            
            // Convert to base64
            val bytes = inputStream.readBytes()
            val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
            
            Log.d("ClarifaiService", "Converted URI to base64, original size: ${bytes.size} bytes")
            base64
            
        } catch (e: Exception) {
            Log.e("ClarifaiService", "Error converting URI to base64", e)
            null
        }
    }
}
