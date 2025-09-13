package com.rookie.code.common.constants

object AppConstants {

    
    // API Configuration
    object Api {
        // Debug flag
        const val IS_DEBUG = true // Set to false for production
        
        // Timeouts (in milliseconds)
        const val REQUEST_TIMEOUT = 30_000L
        const val CONNECT_TIMEOUT = 15_000L
        const val READ_TIMEOUT = 30_000L
    }
    
    // UI Text
    object UiText {

        const val NUTRITION_TITLE = "Nutrition"
        
        // Buttons
        const val PICK_FOOD_IMAGE = "Pick Food Image"

        const val ANALYZE_WITH_GEMINI_AI = "Analyze with Gemini AI"
        const val RETRY_ANALYSIS = "Retry Analysis"
        const val TRY_DIFFERENT_IMAGE = "Try Different Image"
        const val ANALYZE_AGAIN = "Analyze Again"
        
        // Messages
        const val TAKE_PHOTO_OR_SELECT_GALLERY_GEMINI = "Take a photo or select from gallery to get detailed food analysis using Gemini AI"
        const val AI_POWERED_FOOD_ANALYSIS = "AI-Powered Food Analysis"
        const val ANALYZING_FOOD_WITH_GEMINI = "Analyzing food with Gemini AI..."
        const val THIS_MAY_TAKE_FEW_MOMENTS = "This may take a few moments"
        const val ANALYSIS_ERROR = "Analysis Error"
        const val NOT_A_FOOD_IMAGE = "Not a Food Image"
        const val GEMINI_AI_ANALYSIS_RESULTS = "Gemini AI Analysis Results"
        
        // Content Descriptions
        const val CONTENT_DESC_ANALYZE = "Analyze"
        const val CONTENT_DESC_SELECTED_FOOD_IMAGE = "Selected food image"

    }
    

}
