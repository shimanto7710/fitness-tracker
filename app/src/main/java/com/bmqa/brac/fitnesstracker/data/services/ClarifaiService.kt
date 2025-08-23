package com.bmqa.brac.fitnesstracker.data.services

import android.content.Context
import android.net.Uri
import android.util.Log
import com.bmqa.brac.fitnesstracker.data.models.FoodItem
import com.bmqa.brac.fitnesstracker.data.local.FoodNutritionDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import java.io.InputStream

class ClarifaiService(private val context: Context) {
    
    private val clarifai = ClarifaiHelper()
    
    suspend fun recognizeFoodFromImage(imageUri: Uri): List<FoodItem> {
        return suspendCancellableCoroutine { continuation ->
            try {
                // Convert URI to base64
                val base64Image = uriToBase64(imageUri)
                if (base64Image != null) {
                    Log.d("ClarifaiService", "Image converted to base64, size: ${base64Image.length} characters")
                    
                    clarifai.predictImageBase64(base64Image) { result ->
                        Log.d("ClarifaiService", "Prediction result: $result")
                        
                        // Parse the result string and convert to FoodItem objects
                        val foodItems = parseClarifaiResult(result)
                        continuation.resume(foodItems)
                    }
                } else {
                    Log.e("ClarifaiService", "Failed to convert image to base64")
                    continuation.resume(emptyList())
                }
            } catch (e: Exception) {
                Log.e("ClarifaiService", "Error processing image", e)
                continuation.resume(emptyList())
            }
        }
    }
    
    private fun uriToBase64(uri: Uri): String? {
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
    
    private fun parseClarifaiResult(result: String): List<FoodItem> {
        return try {
            // Split the result by newlines and parse each line
            result.split("\n")
                .filter { it.isNotEmpty() && it.contains(":") }
                .mapNotNull { line ->
                    val parts = line.split(": ")
                    if (parts.size == 2) {
                        val name = parts[0].trim()
                        val confidence = parts[1].trim().toFloatOrNull() ?: 0.0f
                        
                        // Get nutrition information from database
                        val nutrition = FoodNutritionDatabase.getFoodNutrition(name)
                        
                        FoodItem(
                            name = name,
                            confidence = confidence,
                            calories = nutrition?.calories,
                            protein = nutrition?.protein,
                            carbs = nutrition?.carbs,
                            fat = nutrition?.fat
                        )
                    } else null
                }
                .filter { it.confidence > 0.1f } // Filter low confidence results
                .sortedByDescending { it.confidence } // Sort by confidence
        } catch (e: Exception) {
            Log.e("ClarifaiService", "Error parsing result: $result", e)
            emptyList()
        }
    }
}
