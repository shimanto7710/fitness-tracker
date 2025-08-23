package com.bmqa.brac.fitnesstracker.data.local

import com.bmqa.brac.fitnesstracker.data.models.FoodNutrition

object FoodNutritionDatabase {
    
    private val foodDatabase = mapOf(
        "cake" to FoodNutrition(calories = 257, protein = "3.2g", carbs = "37.1g", fat = "11.6g"),
        "cookie" to FoodNutrition(calories = 502, protein = "5.7g", carbs = "65.1g", fat = "24.9g"),
        "pastry" to FoodNutrition(calories = 262, protein = "4.1g", carbs = "31.2g", fat = "13.8g"),
        "cheese" to FoodNutrition(calories = 113, protein = "6.8g", carbs = "0.4g", fat = "9.3g"),
        "chocolate" to FoodNutrition(calories = 546, protein = "4.9g", carbs = "61.0g", fat = "31.0g"),
        "bread" to FoodNutrition(calories = 265, protein = "9.0g", carbs = "49.0g", fat = "3.2g"),
        "apple" to FoodNutrition(calories = 52, protein = "0.3g", carbs = "14.0g", fat = "0.2g"),
        "banana" to FoodNutrition(calories = 89, protein = "1.1g", carbs = "23.0g", fat = "0.3g"),
        "orange" to FoodNutrition(calories = 47, protein = "0.9g", carbs = "12.0g", fat = "0.1g"),
        "strawberry" to FoodNutrition(calories = 32, protein = "0.7g", carbs = "7.7g", fat = "0.3g"),
        "pizza" to FoodNutrition(calories = 266, protein = "11.0g", carbs = "33.0g", fat = "10.0g"),
        "burger" to FoodNutrition(calories = 354, protein = "16.8g", carbs = "29.6g", fat = "17.8g"),
        "rice" to FoodNutrition(calories = 130, protein = "2.7g", carbs = "28.0g", fat = "0.3g"),
        "pasta" to FoodNutrition(calories = 131, protein = "5.0g", carbs = "25.0g", fat = "1.1g"),
        "salad" to FoodNutrition(calories = 20, protein = "2.0g", carbs = "3.0g", fat = "0.2g"),
        "soup" to FoodNutrition(calories = 85, protein = "4.0g", carbs = "12.0g", fat = "2.5g"),
        "ice cream" to FoodNutrition(calories = 207, protein = "3.5g", carbs = "24.0g", fat = "11.0g"),
        "coffee" to FoodNutrition(calories = 2, protein = "0.3g", carbs = "0.0g", fat = "0.0g"),
        "tea" to FoodNutrition(calories = 1, protein = "0.0g", carbs = "0.0g", fat = "0.0g"),
        "water" to FoodNutrition(calories = 0, protein = "0.0g", carbs = "0.0g", fat = "0.0g")
    )
    
    fun getFoodNutrition(foodName: String): FoodNutrition? {
        return foodDatabase[foodName.lowercase()]
    }
}
