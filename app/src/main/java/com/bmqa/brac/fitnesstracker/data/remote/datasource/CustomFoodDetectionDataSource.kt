package com.bmqa.brac.fitnesstracker.data.remote.datasource

import android.content.Context
import android.net.Uri
import android.util.Log
import com.bmqa.brac.fitnesstracker.data.remote.api.CustomFoodDetectionApiService
import com.bmqa.brac.fitnesstracker.data.remote.dto.FoodDetectionItem
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface CustomFoodDetectionDataSource {
    suspend fun detectFoodFromImage(imageUri: String, base64Image: String): Result<List<FoodItem>>
}

class CustomFoodDetectionDataSourceImpl @Inject constructor(
    private val apiService: CustomFoodDetectionApiService
) : CustomFoodDetectionDataSource {
    
    override suspend fun detectFoodFromImage(imageUri: String, base64Image: String): Result<List<FoodItem>> {
        return try {
            Log.d("CustomFoodDetection", "Starting food detection for image: $imageUri")
            
            // Convert base64 to File
            val imageFile = base64ToFile(base64Image)
            
            // Create MultipartBody.Part for the image
            val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("file", imageFile.name, requestBody)
            
            Log.d("CustomFoodDetection", "Sending request to API...")
            
            // Call the API
            val response = apiService.detectFood(imagePart)
            
            Log.d("CustomFoodDetection", "API response received: ${response.message}")
            
            // Parse the response and convert to FoodItem entities
            val foodItems = response.getFoodItems().map { detectionItem ->
                FoodItem(
                    name = detectionItem.label,
                    confidence = 0.9f, // Default confidence for custom API
                    boundingBox = if (detectionItem.bbox_2d.size >= 4) {
                        com.bmqa.brac.fitnesstracker.domain.entities.BoundingBox(
                            x = detectionItem.bbox_2d[0].toFloat(),
                            y = detectionItem.bbox_2d[1].toFloat(),
                            width = (detectionItem.bbox_2d[2] - detectionItem.bbox_2d[0]).toFloat(),
                            height = (detectionItem.bbox_2d[3] - detectionItem.bbox_2d[1]).toFloat()
                        )
                    } else null,
                    calories = null, // Not provided by this API
                    protein = null,  // Not provided by this API
                    carbs = null,    // Not provided by this API
                    fat = null       // Not provided by this API
                )
            }
            
            Log.d("CustomFoodDetection", "Successfully detected ${foodItems.size} food items")
            Result.success(foodItems)
            
        } catch (e: Exception) {
            Log.e("CustomFoodDetection", "Error detecting food", e)
            Result.failure(e)
        }
    }
    
    private fun base64ToFile(base64Image: String): File {
        val file = File.createTempFile("food_image", ".jpg")
        val bytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
        
        FileOutputStream(file).use { output ->
            output.write(bytes)
        }
        
        return file
    }
}
