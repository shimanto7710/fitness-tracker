package com.bmqa.brac.fitnesstracker.data.mapper

import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiResponse
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.FoodNutrition

/**
 * Mapper class to convert Clarifai API response DTOs to domain entities
 * Following Clean Architecture principles - data layer should not know about domain
 */
class ClarifaiMapper {
    
    /**
     * Maps Clarifai API response to list of FoodItem domain entities
     */
    fun mapToFoodItems(
        response: ClarifaiResponse,
        nutritionDataSource: (String) -> FoodNutrition?
    ): List<FoodItem> {
        return response.outputs.firstOrNull()?.data?.concepts?.mapNotNull { concept ->
            val nutrition = nutritionDataSource(concept.name)
            FoodItem(
                name = concept.name,
                confidence = concept.value,
                calories = nutrition?.calories,
                protein = nutrition?.protein,
                carbs = nutrition?.carbs,
                fat = nutrition?.fat
            )
        }?.filter { it.confidence > 0.1f }
         ?.sortedByDescending { it.confidence }
         ?: emptyList()
    }
}
