package com.bmqa.brac.foodlens.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.bmqa.brac.foodlens.common.utils.JsonUtils
import com.bmqa.brac.foodlens.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.foodlens.presentation.navigation.NavigationDataManager

/**
 * Navigation extensions for type-safe navigation
 */

/**
 * Navigate to a route with proper navigation options
 */
fun NavController.navigateToRoute(
    route: Route,
    builder: NavOptionsBuilder.() -> Unit = {
        // Default navigation options
        launchSingleTop = true
        restoreState = true
    }
) {
    navigate(route, builder)
}

/**
 * Navigate to home screen (calendar)
 */
fun NavController.navigateToHome() {
    navigateToRoute(Route.Home)
}



/**
 * Navigate to Gemini food analysis screen
 */
fun NavController.navigateToGeminiFoodAnalysis(parent: String? = null, imageUri: String? = null, selectedDate: String? = null) {
    navigateToRoute(Route.GeminiFoodAnalysis(parent, imageUri, selectedDate))
}

/**
 * Navigate to nutrition details screen
 */
fun NavController.navigateToNutrition(
    parent: String? = null,
    geminiAnalysis: String? = null
) {
    navigateToRoute(Route.Nutrition(parent, geminiAnalysis))
}

/**
 * Navigate to nutrition details screen with GeminiFoodAnalysis data
 */
fun NavController.navigateToNutritionWithAnalysis(
    geminiAnalysis: GeminiFoodAnalysis,
    parent: String? = null
) {
    val analysisId = NavigationDataManager.storeAnalysis(geminiAnalysis)
    navigateToRoute(Route.Nutrition(parent, analysisId)) {
        // Pop the GeminiFoodAnalysis screen from the back stack
        popUpTo<Route.GeminiFoodAnalysis> {
            inclusive = true
        }
    }
}


/**
 * Navigate back with proper handling
 */
fun NavController.navigateBack() {
    if (!popBackStack()) {
        // If we can't pop back, we're at the root
        // Handle this case as needed
    }
}

/**
 * Navigate to home and clear back stack
 */
fun NavController.navigateToHomeAndClearStack() {
    navigate(Route.Home) {
        popUpTo(0) {
            inclusive = true
        }
    }
}

