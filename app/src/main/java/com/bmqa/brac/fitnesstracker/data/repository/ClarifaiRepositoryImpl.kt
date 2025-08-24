package com.bmqa.brac.fitnesstracker.data.repository

import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.data.local.datasource.FoodNutritionDataSource
import com.bmqa.brac.fitnesstracker.data.remote.datasource.ClarifaiRemoteDataSource
import com.bmqa.brac.fitnesstracker.data.mapper.ClarifaiMapper
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.domain.repository.ClarifaiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClarifaiRepositoryImpl @Inject constructor(
    private val remoteDataSource: ClarifaiRemoteDataSource,
    private val nutritionDataSource: FoodNutritionDataSource,
    private val clarifaiMapper: ClarifaiMapper
) : ClarifaiRepository {
    
    override suspend fun recognizeFoodFromBase64(base64Image: String): Result<List<FoodItem>> = withContext(Dispatchers.IO) {
        try {
            val clarifaiResponse = remoteDataSource.recognizeFood(base64Image)
            clarifaiResponse.map { response ->
                clarifaiMapper.mapToFoodItems(response) { foodName ->
                    nutritionDataSource.getFoodNutrition(foodName)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
