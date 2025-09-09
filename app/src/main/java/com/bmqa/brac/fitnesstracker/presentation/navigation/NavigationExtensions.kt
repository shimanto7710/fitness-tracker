package com.bmqa.brac.fitnesstracker.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.bmqa.brac.fitnesstracker.common.utils.JsonUtils
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis

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
 * Navigate to dashboard screen
 */
fun NavController.navigateToDashboard() {
    navigateToRoute(Route.Dashboard)
}

/**
 * Navigate to calories management screen
 */
fun NavController.navigateToCaloriesManagement(parent: String? = null) {
    navigateToRoute(Route.CaloriesManagement(parent))
}

/**
 * Navigate to Gemini food analysis screen
 */
fun NavController.navigateToGeminiFoodAnalysis(parent: String? = null, imageUri: String? = null) {
    navigateToRoute(Route.GeminiFoodAnalysis(parent, imageUri))
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
    val serializedAnalysis = JsonUtils.serializeGeminiFoodAnalysis(geminiAnalysis)
    navigateToRoute(Route.Nutrition(parent, serializedAnalysis)) {
        // Pop the GeminiFoodAnalysis screen from the back stack
        popUpTo(Route.GeminiFoodAnalysis::class) {
            inclusive = true
        }
    }
}


/**
 * Navigate to database test screen
 */
fun NavController.navigateToDatabaseTest() {
    navigateToRoute(Route.DatabaseTest)
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

