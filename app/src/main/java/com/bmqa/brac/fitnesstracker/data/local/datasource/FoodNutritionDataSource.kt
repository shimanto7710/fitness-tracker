package com.bmqa.brac.fitnesstracker.data.local.datasource

import com.bmqa.brac.fitnesstracker.domain.entities.FoodNutrition

interface FoodNutritionDataSource {
    fun getFoodNutrition(foodName: String): FoodNutrition?
}
