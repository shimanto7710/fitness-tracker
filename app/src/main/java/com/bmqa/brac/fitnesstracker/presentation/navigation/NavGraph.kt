package com.bmqa.brac.fitnesstracker.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bmqa.brac.fitnesstracker.common.utils.JsonUtils
import com.bmqa.brac.fitnesstracker.presentation.ui.screens.CaloriesManagementScreen
import com.bmqa.brac.fitnesstracker.presentation.ui.screens.DashboardScreen
import com.bmqa.brac.fitnesstracker.presentation.ui.screens.DatabaseTestScreen
import com.bmqa.brac.fitnesstracker.presentation.ui.screens.GeminiFoodAnalysisScreen
import com.bmqa.brac.fitnesstracker.presentation.ui.screens.HomeScreen
import com.bmqa.brac.fitnesstracker.presentation.ui.screens.NutritionDetailsScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Home,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(220, delayMillis = 90)
            ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(220)) + fadeOut(
                animationSpec = tween(220)
            )
        },
    ) {
        composable<Route.Home> {
            HomeScreen(
                onNavigateToCaloriesManagement = {
                    navController.navigateToCaloriesManagement()
                },
                onNavigateToGeminiFoodAnalysis = { imageUri ->
                    navController.navigateToGeminiFoodAnalysis(imageUri = imageUri)
                },
                onNavigateToNutrition = { analysis ->
                    navController.navigateToNutritionWithAnalysis(analysis)
                },
                onNavigateToDashboard = {
                    navController.navigateToDashboard()
                },
                onNavigateToDatabaseTest = {
                    navController.navigateToDatabaseTest()
                }
            )
        }

        composable<Route.Dashboard> {
            DashboardScreen(
                onNavigateToCaloriesManagement = {
                    navController.navigateToCaloriesManagement()
                },
                onNavigateToGeminiFoodAnalysis = {
                    navController.navigateToGeminiFoodAnalysis()
                },
                onNavigateToNutrition = {
                    navController.navigateToNutrition()
                },
                onNavigateToCalendar = {
                    navController.navigateToHome()
                },
                onNavigateToDatabaseTest = {
                    navController.navigateToDatabaseTest()
                }
            )
        }

        composable<Route.CaloriesManagement> {
            val args = it.toRoute<Route.CaloriesManagement>()
            CaloriesManagementScreen(
                onNavigateBack = {
                    navController.navigateBack()
                }
            )
        }

        composable<Route.GeminiFoodAnalysis> {
            val args = it.toRoute<Route.GeminiFoodAnalysis>()
            val imageUri = args.imageUri?.let { uriString ->
                try {
                    android.net.Uri.parse(uriString)
                } catch (e: Exception) {
                    null
                }
            }
            GeminiFoodAnalysisScreen(
                onNavigateBack = {
                    navController.navigateBack()
                },
                onNavigateToNutritionDetails = { geminiAnalysis ->
                    navController.navigateToNutritionWithAnalysis(geminiAnalysis)
                },
                preSelectedImageUri = imageUri
            )
        }

        composable<Route.Nutrition> {
            val args = it.toRoute<Route.Nutrition>()
            val geminiAnalysis = args.geminiAnalysis?.let { jsonString ->
                JsonUtils.deserializeGeminiFoodAnalysis(jsonString)
            }
            
            NutritionDetailsScreen(
                geminiAnalysis = geminiAnalysis,
                onNavigateBack = {
                    navController.navigateBack()
                }
            )
        }


        composable<Route.DatabaseTest> {
            DatabaseTestScreen()
        }
    }
}
