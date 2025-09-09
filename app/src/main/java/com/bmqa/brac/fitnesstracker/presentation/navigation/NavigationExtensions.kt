package com.bmqa.brac.fitnesstracker.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

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
 * Navigate to home screen
 */
fun NavController.navigateToHome() {
    navigateToRoute(Route.Home)
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
fun NavController.navigateToGeminiFoodAnalysis(parent: String? = null) {
    navigateToRoute(Route.GeminiFoodAnalysis(parent))
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
 * Navigate to calendar screen
 */
fun NavController.navigateToCalendar() {
    navigateToRoute(Route.Calendar)
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

