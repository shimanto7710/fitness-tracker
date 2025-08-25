package com.bmqa.brac.fitnesstracker.data.remote.dto

data class CustomFoodDetectionResponse(
    val message: String,
    val model: String,
    val usage: Usage,
    val finish_reason: String
) {
    // Parse the JSON from the message field to extract food items
    fun getFoodItems(): List<FoodDetectionItem> {
        return try {
            // Extract JSON content from the markdown code block
            val jsonStart = message.indexOf("```json\n") + 8
            val jsonEnd = message.indexOf("\n```", jsonStart)
            val jsonContent = message.substring(jsonStart, jsonEnd)
            
            // Parse the JSON array
            val gson = com.google.gson.Gson()
            gson.fromJson(jsonContent, Array<FoodDetectionItem>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

data class FoodDetectionItem(
    val bbox_2d: List<Int>, // [x1, y1, x2, y2] coordinates
    val label: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)
