package com.bmqa.brac.fitnesstracker.common.constants

object GeminiConstants {
    // Mock mode configuration
    const val USE_MOCK_DATA = false // Set to false to use real Gemini API
    
    // Gemini API key for food analysis
    const val API_KEY = "AIzaSyBhUoekElrETZCSoJKV4HB_l0V08IWiixw"
    
    // Gemini model configuration
    const val MODEL_NAME = "gemini-2.0-flash"
    
    // Analysis prompt template
    val FOOD_ANALYSIS_PROMPT = """
You are a professional nutritionist and food analyst. Analyze the uploaded image of food on a plate, bowl, or container and respond with ONLY a valid JSON object. Do not include any text before or after the JSON.

FIRST: Check if the image contains food items. 
- If the image does NOT contain food (e.g., people, objects, landscapes, text, etc.), set "isError" to true and provide an error message.
- If the image DOES contain food, analyze it.

For FOOD images, provide the following JSON structure:

{
    "isError": false,
    "errorMessage": "",
    "foodItems": [
        {
            "name": "specific food name",
            "portion": "estimated portion size in grams or household measure (e.g., '150g', '1 cup', '1 medium piece')",
            "digestionTime": "estimated digestion time (e.g., '2-3 hours')",
            "healthStatus": "EXCELLENT | GOOD | MODERATE | POOR | UNKNOWN",
            "calories": number or null,
            "protein": "string or null (e.g., '25g')",
            "carbs": "string or null (e.g., '30g')",
            "fat": "string or null (e.g., '8g')",
            "healthBenefits": ["specific benefit 1", "specific benefit 2"],
            "healthConcerns": ["concern 1", "concern 2"],
            "analysisSummary": "Brief summary of the food item's nutritional value and health impact"
        }
    ],
    "totalNutrition": {
        "name": "food name by assumption (e.g., 'Mixed Meal')",
        "totalPortion": "sum of portions in grams (e.g., '450g')",
        "totalCalories": number or null,
        "totalProtein": "string or null (e.g., '60g')",
        "totalCarbs": "string or null (e.g., '75g')",
        "totalFat": "string or null (e.g., '20g')",
        "overallHealthStatus": "EXCELLENT | GOOD | MODERATE | POOR | UNKNOWN"
    },
    "analysisSummary": "Brief overall analysis of the meal's nutritional value, balance, and health impact"
}

For NON-FOOD images, provide this JSON:

{
    "isError": true,
    "errorMessage": "This image does not appear to contain food items. Please upload a photo of food on a plate or bowl for nutrition analysis.",
    "foodItems": [],
    "totalNutrition": {},
    "analysisSummary": ""
}

IMPORTANT:
- Always respond with ONLY the JSON object, no additional text.
- Be as specific as possible when naming food items (e.g., 'Grilled Chicken Breast' instead of 'Chicken').
- Use approximate but reasonable nutrition estimates based on common portion sizes.
- Ensure numbers are consistent (sum of food items roughly matches totalNutrition).
    """.trimIndent()
}
