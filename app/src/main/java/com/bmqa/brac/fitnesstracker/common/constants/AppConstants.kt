package com.bmqa.brac.fitnesstracker.common.constants

object AppConstants {
    
    // Navigation
    object Navigation {
        const val SCREEN_HOME = "home"
        const val SCREEN_CALORIES_MANAGEMENT = "calories_management"
        const val SCREEN_GEMINI_FOOD_ANALYSIS = "gemini_food_analysis"
    }
    
    // API Configuration
    object Api {
        const val BASE_URL = "https://api.clarifai.com/"
        const val MODEL_ID = "food-item-recognition"
        const val USER_ID = "clarifai"
        const val APP_ID = "main"
        const val API_KEY = "57aee404b0c44eddb317dc69f04895b5"
        const val AUTHORIZATION_HEADER = "Key $API_KEY"
        
        // Timeouts
        const val CONNECT_TIMEOUT = 30L
        const val READ_TIMEOUT = 30L
        const val WRITE_TIMEOUT = 30L
    }
    
    // UI Text
    object UiText {
        // Common
        const val LOADING = "Loading..."
        const val ERROR = "Error"
        const val SUCCESS = "Success"
        const val CANCEL = "Cancel"
        const val OK = "OK"
        const val BACK = "Back"
        const val NEXT = "Next"
        const val SAVE = "Save"
        const val DELETE = "Delete"
        const val EDIT = "Edit"
        const val ADD = "Add"
        
        // Navigation
        const val NAVIGATION_TITLE = "Fitness Tracker"
        const val IMAGE_PICKER_TITLE = "Image Picker"
        const val CALORIES_MANAGEMENT_TITLE = "Calories Management"
        const val GEMINI_FOOD_ANALYSIS_TITLE = "Gemini Food Analysis"
        
        // Buttons
        const val SELECT_IMAGE = "Select Image"
        const val PICK_FOOD_IMAGE = "Pick Food Image"
        const val ANALYZE_FOOD = "Analyze Food"
        const val ANALYZE_WITH_GEMINI_AI = "Analyze with Gemini AI"
        const val RETRY_ANALYSIS = "Retry Analysis"
        const val TRY_DIFFERENT_IMAGE = "Try Different Image"
        const val ANALYZE_AGAIN = "Analyze Again"
        const val RETRY = "Retry"
        const val START_IMAGE_PICKER = "Start Image Picker"
        
        // Messages
        const val IMAGE_SELECTED = "Image selected: %s"
        const val NO_IMAGE_SELECTED = "No Image Selected"
        const val SELECT_IMAGE_TO_ANALYZE = "Select an image to analyze food"
        const val ANALYZING_FOOD = "Analyzing food..."
        const val FOOD_DETECTED = "Food detected: %s"
        const val NO_FOOD_DETECTED = "No food detected"
        const val ERROR_ANALYZING_FOOD = "Error analyzing food"
        const val CHOOSE_YOUR_FEATURE = "Choose your feature"
        const val ANALYZE_FOOD_IMAGES_FOR_CALORIES = "Analyze food images for calorie tracking"
        const val ADVANCED_AI_FOOD_ANALYSIS = "Advanced AI food analysis with Google Gemini"
        const val TAKE_PHOTO_OR_SELECT_GALLERY_GEMINI = "Take a photo or select from gallery to get detailed food analysis using Gemini AI"
        const val AI_POWERED_FOOD_ANALYSIS = "AI-Powered Food Analysis"
        const val ANALYZING_FOOD_WITH_GEMINI = "Analyzing food with Gemini AI..."
        const val THIS_MAY_TAKE_FEW_MOMENTS = "This may take a few moments"
        const val ANALYSIS_ERROR = "Analysis Error"
        const val NOT_A_FOOD_IMAGE = "⚠️ Not a Food Image"
        const val GEMINI_AI_ANALYSIS_RESULTS = "Gemini AI Analysis Results"
        const val DETECTION_RESULTS = "Detection Results"
        const val FOUND_FOOD_ITEMS = "Found %d food item(s)"
        const val NUTRITION_INFO_NOT_AVAILABLE = "Nutrition info not available from this API"
        
        // Food Recognition
        const val DETECTED_FOODS = "Detected Foods (%d)"
        const val CONFIDENCE = "Confidence: %d%%"
        const val CALORIES = "Calories: %d"
        const val PROTEIN = "Protein: %s"
        const val CARBS = "Carbs: %s"
        const val FAT = "Fat: %s"
        
        // Content Descriptions
        const val CONTENT_DESC_NAVIGATE = "Navigate"
        const val CONTENT_DESC_GALLERY = "Gallery"
        const val CONTENT_DESC_GEMINI_FOOD_ANALYSIS = "Gemini Food Analysis"
        const val CONTENT_DESC_ANALYZE = "Analyze"
        const val CONTENT_DESC_SELECTED_FOOD_IMAGE = "Selected food image"
        const val CONTENT_DESC_START = "Start"
        
        // Nutrition Labels
        const val LABEL_PORTION = "Portion:"
        const val LABEL_DIGESTION = "Digestion:"
        const val LABEL_NUTRITION_INFORMATION = "Nutrition Information"
        const val LABEL_HEALTH_BENEFITS = "Health Benefits"
        const val LABEL_HEALTH_CONCERNS = "Health Concerns"
        const val LABEL_LOCATION = "Location: x=%s, y=%s, w=%s, h=%s"
        

    }
    
    // Dimensions
    object Dimensions {
        // Padding
        const val PADDING_SMALL = 8
        const val PADDING_MEDIUM = 16
        const val PADDING_LARGE = 24
        const val PADDING_XLARGE = 32
        
        // Margins
        const val MARGIN_SMALL = 8
        const val MARGIN_MEDIUM = 16
        const val MARGIN_LARGE = 24
        
        // Heights
        const val IMAGE_HEIGHT = 200
        const val FOOD_LIST_HEIGHT = 300
        const val CARD_HEIGHT = 200
        
        // Sizes
        const val ICON_SIZE_SMALL = 20
        const val ICON_SIZE_MEDIUM = 24
        const val ICON_SIZE_LARGE = 48
        const val ICON_SIZE_XLARGE = 64
    }
    
    // Error Messages
    object ErrorMessages {
        const val NETWORK_ERROR = "Network error occurred"
        const val API_ERROR = "API error occurred"
        const val UNKNOWN_ERROR = "Unknown error occurred"
        const val IMAGE_LOADING_ERROR = "Error loading image"
        const val FOOD_RECOGNITION_ERROR = "Error recognizing food"
        const val EMPTY_RESPONSE = "Empty response from server"
        const val INVALID_IMAGE = "Invalid image format"
    }
    
    // Success Messages
    object SuccessMessages {
        const val IMAGE_LOADED = "Image loaded successfully"
        const val FOOD_RECOGNIZED = "Food recognized successfully"
        const val API_CALL_SUCCESS = "API call successful"
    }
    
    // File Types
    object FileTypes {
        const val IMAGE_MIME_TYPE = "image/*"
        const val JPEG_MIME_TYPE = "image/jpeg"
        const val PNG_MIME_TYPE = "image/png"
    }
    

}
