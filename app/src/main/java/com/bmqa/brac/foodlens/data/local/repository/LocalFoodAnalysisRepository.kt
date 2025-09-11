package com.bmqa.brac.foodlens.data.local.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.bmqa.brac.foodlens.data.local.database.FoodLensDatabase
import com.bmqa.brac.foodlens.data.local.database.dao.CompleteFoodAnalysis
import com.bmqa.brac.foodlens.data.local.database.entities.FoodAnalysisEntity
import com.bmqa.brac.foodlens.data.local.database.entities.FoodItemEntity
import com.bmqa.brac.foodlens.data.local.database.entities.TotalNutritionEntity
import com.bmqa.brac.foodlens.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.foodlens.domain.entities.GeminiFoodItem
import com.bmqa.brac.foodlens.domain.entities.TotalNutrition
import com.bmqa.brac.foodlens.domain.repository.FoodAnalysisRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class LocalFoodAnalysisRepository(private val context: Context) : FoodAnalysisRepository {

    private val database = FoodLensDatabase.getDatabase(context)
    private val dao = database.foodAnalysisDao()

    // Implementation of domain interface
    override suspend fun saveFoodAnalysis(
        foodAnalysis: GeminiFoodAnalysis,
        imageUri: String,
        imageBitmap: Bitmap?
    ): Unit = withContext(Dispatchers.IO) {
        
        // Save image to internal storage if provided
        val imagePath = if (imageBitmap != null) {
            saveImageToInternalStorage(imageBitmap, imageUri)
        } else null

        // Convert image to base64 if provided
        val base64Image = if (imageBitmap != null) {
            convertBitmapToBase64(imageBitmap)
        } else null

        // Insert main food analysis entity
        val foodAnalysisEntity = FoodAnalysisEntity(
            isError = foodAnalysis.isError,
            errorMessage = foodAnalysis.errorMessage,
            analysisSummary = foodAnalysis.analysisSummary,
            dateNTime = foodAnalysis.dateNTime,
            imagePath = imagePath,
            imageUri = imageUri,
            base64Image = base64Image,
            selectedDate = foodAnalysis.selectedDate
        )
        
        val analysisId = dao.insertFoodAnalysis(foodAnalysisEntity)

        // Insert food items
        val foodItemEntities = foodAnalysis.foodItems.map { item ->
            FoodItemEntity(
                analysisId = analysisId,
                name = item.name,
                portion = item.portion,
                digestionTime = item.digestionTime,
                healthStatus = item.healthStatus,
                calories = item.calories,
                protein = item.protein,
                carbs = item.carbs,
                fat = item.fat,
                healthBenefits = item.healthBenefits,
                healthConcerns = item.healthConcerns,
                analysisSummary = item.analysisSummary
            )
        }
        dao.insertFoodItems(foodItemEntities)

        // Insert total nutrition if available
        foodAnalysis.totalNutrition?.let { totalNutrition ->
            val totalNutritionEntity = TotalNutritionEntity(
                analysisId = analysisId,
                name = totalNutrition.name,
                totalPortion = totalNutrition.totalPortion,
                totalCalories = totalNutrition.totalCalories,
                totalProtein = totalNutrition.totalProtein,
                totalCarbs = totalNutrition.totalCarbs,
                totalFat = totalNutrition.totalFat,
                overallHealthStatus = totalNutrition.overallHealthStatus
            )
            dao.insertTotalNutrition(totalNutritionEntity)
        }

        // Return Unit as per interface
    }

    // Additional method for testing that returns the ID
    suspend fun saveFoodAnalysisWithId(
        foodAnalysis: GeminiFoodAnalysis,
        imageUri: String? = null,
        imageBitmap: Bitmap? = null
    ): Long = withContext(Dispatchers.IO) {
        
        // Save image to internal storage if provided
        val imagePath = if (imageBitmap != null) {
            saveImageToInternalStorage(imageBitmap, imageUri)
        } else null

        // Convert image to base64 if provided
        val base64Image = if (imageBitmap != null) {
            convertBitmapToBase64(imageBitmap)
        } else null

        // Insert main food analysis entity
        val foodAnalysisEntity = FoodAnalysisEntity(
            isError = foodAnalysis.isError,
            errorMessage = foodAnalysis.errorMessage,
            analysisSummary = foodAnalysis.analysisSummary,
            dateNTime = foodAnalysis.dateNTime,
            imagePath = imagePath,
            imageUri = imageUri,
            base64Image = base64Image,
            selectedDate = foodAnalysis.selectedDate
        )
        
        val analysisId = dao.insertFoodAnalysis(foodAnalysisEntity)

        // Insert food items
        val foodItemEntities = foodAnalysis.foodItems.map { item ->
            FoodItemEntity(
                analysisId = analysisId,
                name = item.name,
                portion = item.portion,
                digestionTime = item.digestionTime,
                healthStatus = item.healthStatus,
                calories = item.calories,
                protein = item.protein,
                carbs = item.carbs,
                fat = item.fat,
                healthBenefits = item.healthBenefits,
                healthConcerns = item.healthConcerns,
                analysisSummary = item.analysisSummary
            )
        }
        dao.insertFoodItems(foodItemEntities)

        // Insert total nutrition if available
        foodAnalysis.totalNutrition?.let { totalNutrition ->
            val totalNutritionEntity = TotalNutritionEntity(
                analysisId = analysisId,
                name = totalNutrition.name,
                totalPortion = totalNutrition.totalPortion,
                totalCalories = totalNutrition.totalCalories,
                totalProtein = totalNutrition.totalProtein,
                totalCarbs = totalNutrition.totalCarbs,
                totalFat = totalNutrition.totalFat,
                overallHealthStatus = totalNutrition.overallHealthStatus
            )
            dao.insertTotalNutrition(totalNutritionEntity)
        }

        analysisId
    }

    // Get all food analyses
    fun getAllFoodAnalyses(): Flow<List<GeminiFoodAnalysis>> {
        return dao.getAllCompleteFoodAnalyses().map { completeAnalyses ->
            completeAnalyses.map { it.toDomainModel() }
        }
    }

    // Get recent food analyses
    fun getRecentFoodAnalyses(limit: Int = 10): Flow<List<GeminiFoodAnalysis>> {
        return dao.getRecentCompleteFoodAnalyses(limit).map { completeAnalyses ->
            completeAnalyses.map { it.toDomainModel() }
        }
    }

    // Get food analysis by ID
    suspend fun getFoodAnalysisById(id: Long): GeminiFoodAnalysis? = withContext(Dispatchers.IO) {
        dao.getCompleteFoodAnalysisById(id)?.toDomainModel()
    }

    // Delete food analysis
    suspend fun deleteFoodAnalysis(id: Long) = withContext(Dispatchers.IO) {
        // Get the analysis to delete associated image
        val analysis = dao.getFoodAnalysisById(id)
        analysis?.imagePath?.let { imagePath ->
            deleteImageFromInternalStorage(imagePath)
        }
        
        dao.deleteFoodAnalysisById(id)
    }

    // Get image bitmap from stored path
    suspend fun getImageBitmap(imagePath: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val file = File(context.filesDir, imagePath)
            if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath)
            } else null
        } catch (e: Exception) {
            null
        }
    }

    // Private helper methods
    private suspend fun saveImageToInternalStorage(bitmap: Bitmap, originalUri: String?): String {
        val fileName = "food_analysis_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, "images/$fileName")
        
        // Create images directory if it doesn't exist
        file.parentFile?.mkdirs()
        
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        
        return "images/$fileName"
    }

    private suspend fun deleteImageFromInternalStorage(imagePath: String) {
        try {
            val file = File(context.filesDir, imagePath)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            // Log error if needed
        }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val byteArray = outputStream.toByteArray()
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
    }

    // Extension function to convert database model to domain model
    private fun CompleteFoodAnalysis.toDomainModel(): GeminiFoodAnalysis {
        return GeminiFoodAnalysis(
            id = foodAnalysis.id,
            isError = foodAnalysis.isError,
            errorMessage = foodAnalysis.errorMessage,
            foodItems = foodItems.map { item ->
                GeminiFoodItem(
                    name = item.name,
                    portion = item.portion,
                    digestionTime = item.digestionTime,
                    healthStatus = item.healthStatus,
                    calories = item.calories,
                    protein = item.protein,
                    carbs = item.carbs,
                    fat = item.fat,
                    healthBenefits = item.healthBenefits,
                    healthConcerns = item.healthConcerns,
                    analysisSummary = item.analysisSummary
                )
            },
            totalNutrition = totalNutrition?.let { total ->
                TotalNutrition(
                    name = total.name,
                    totalPortion = total.totalPortion,
                    totalCalories = total.totalCalories,
                    totalProtein = total.totalProtein,
                    totalCarbs = total.totalCarbs,
                    totalFat = total.totalFat,
                    overallHealthStatus = total.overallHealthStatus
                )
            },
            analysisSummary = foodAnalysis.analysisSummary,
            dateNTime = foodAnalysis.dateNTime,
            imageUri = foodAnalysis.imageUri,
            imagePath = foodAnalysis.imagePath,
            base64Image = foodAnalysis.base64Image,
            selectedDate = foodAnalysis.selectedDate
        )
    }
}

