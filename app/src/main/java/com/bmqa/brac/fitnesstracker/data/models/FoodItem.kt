package com.bmqa.brac.fitnesstracker.data.models

data class FoodItem(
    val name: String,
    val confidence: Float,
    val boundingBox: BoundingBox? = null,
    val calories: Int? = null,
    val protein: String? = null,
    val carbs: String? = null,
    val fat: String? = null
)

data class BoundingBox(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float
)
