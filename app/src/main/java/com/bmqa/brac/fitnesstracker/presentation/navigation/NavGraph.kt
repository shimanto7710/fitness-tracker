package com.bmqa.brac.fitnesstracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.presentation.ui.screens.CaloriesManagementScreen
import com.bmqa.brac.fitnesstracker.presentation.ui.screens.FoodDetectionScreen
import com.bmqa.brac.fitnesstracker.presentation.ui.screens.HomeScreen


sealed class Screen(val route: String) {
    object Home : Screen(AppConstants.Navigation.SCREEN_HOME)
    object CaloriesManagement : Screen(AppConstants.Navigation.SCREEN_CALORIES_MANAGEMENT)
    object FoodDetection : Screen("food_detection")
    
    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route?.substringBefore("/")) {
                Home.route -> Home
                CaloriesManagement.route -> CaloriesManagement
                FoodDetection.route -> FoodDetection
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
        }
    }
}

@Composable
fun FitnessTrackerNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCaloriesManagement = {
                    navController.navigate(Screen.CaloriesManagement.route)
                },
                onNavigateToFoodDetection = {
                    navController.navigate(Screen.FoodDetection.route)
                }
            )
        }
        
        composable(Screen.CaloriesManagement.route) {
            CaloriesManagementScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.FoodDetection.route) {
            FoodDetectionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
