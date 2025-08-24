package com.bmqa.brac.fitnesstracker.data.local.datasource

import com.bmqa.brac.fitnesstracker.domain.entities.FoodNutrition
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodNutritionDataSourceImpl @Inject constructor() : FoodNutritionDataSource {
    
    override fun getFoodNutrition(foodName: String): FoodNutrition? {
        // TODO: Implement real data source connection
        // This could be connected to a local database, API, or other data source
        return null
    }
}
