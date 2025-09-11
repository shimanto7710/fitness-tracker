package com.bmqa.brac.foodlens.domain.entities

data class FoodItem(
    val name: String,
    val confidence: Float,
    val boundingBox: BoundingBox? = null
)

data class BoundingBox(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float
)
