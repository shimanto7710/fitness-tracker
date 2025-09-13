package com.rookie.code.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.rookie.code.domain.entities.GeminiFoodAnalysis
import com.rookie.code.presentation.navigation.NavigationDataManager

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


fun NavController.navigateToGeminiFoodAnalysis(parent: String? = null, imageUri: String? = null, selectedDate: String? = null) {
    navigateToRoute(Route.GeminiFoodAnalysis(parent, imageUri, selectedDate))
}


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

fun NavController.navigateBack() {
    if (!popBackStack()) {
        // If we can't pop back, we're at the root
        // Handle this case as needed
    }
}

