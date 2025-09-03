package com.bmqa.brac.fitnesstracker.common.constants

object GeminiConstants {
    // Gemini API key for food analysis
    const val API_KEY = "AIzaSyBhUoekElrETZCSoJKV4HB_l0V08IWiixw"
    
    // Gemini model configuration
    const val MODEL_NAME = "gemini-2.0-flash"
    
    // Analysis prompt template
    val FOOD_ANALYSIS_PROMPT = """
        You are a professional nutritionist and food analyst. Analyze this image and determine if it contains food items that can be analyzed for nutrition.
        
        IMPORTANT: You MUST respond with ONLY a valid JSON object. Do not include any text before or after the JSON.
        
        FIRST: Check if the image contains food items on a plate, bowl, or food container. If the image does NOT contain food (e.g., people, objects, landscapes, text, etc.), set "isError" to true and provide an error message.
        
        If the image contains food, analyze each food item and provide:
        1. Name of the food (be specific, e.g., "Grilled Chicken Breast" not just "Chicken")
        2. Estimated portion size (e.g., "1 medium piece", "1 cup", "150g")
        3. Estimated digestion time (e.g., "2-3 hours", "3-4 hours")
        4. Health status: EXCELLENT, GOOD, MODERATE, POOR, or UNKNOWN
        5. Estimated calories (number only, or null if unknown)
        6. Protein content (e.g., "25g", "15g", or null if unknown)
        7. Carbohydrates content (e.g., "30g", "20g", or null if unknown)
        8. Fat content (e.g., "8g", "12g", or null if unknown)
        9. Health benefits (array of 2-3 specific benefits)
        10. Health concerns (array of 1-2 concerns if any, empty array if none)
        
        Respond with ONLY this JSON structure:
        
        For NON-FOOD images:
        {
            "isError": true,
            "errorMessage": "This image does not appear to contain food items. Please upload a photo of food on a plate or bowl for nutrition analysis.",
            "foodItems": [],
            "analysisSummary": ""
        }
        
        For FOOD images:
        {
            "isError": false,
            "errorMessage": "",
            "foodItems": [
                {
                    "name": "specific food name",
                    "portion": "estimated portion size",
                    "digestionTime": "estimated digestion time",
                    "healthStatus": "EXCELLENT|GOOD|MODERATE|POOR|UNKNOWN",
                    "calories": number or null,
                    "protein": "string or null",
                    "carbs": "string or null",
                    "fat": "string or null",
                    "healthBenefits": ["specific benefit 1", "specific benefit 2"],
                    "healthConcerns": ["concern 1", "concern 2"]
                }
            ],
            "analysisSummary": "Brief overall analysis of the meal's nutritional value and health impact"
        }
    """.trimIndent()
}
