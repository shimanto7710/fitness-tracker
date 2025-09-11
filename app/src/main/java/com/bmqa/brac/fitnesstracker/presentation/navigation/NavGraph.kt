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
import com.bmqa.brac.fitnesstracker.presentation.navigation.NavigationDataManager
import com.bmqa.brac.fitnesstracker.presentation.features.foodanalysis.ui.screens.GeminiFoodAnalysisScreen
import com.bmqa.brac.fitnesstracker.presentation.features.home.ui.screens.HomeScreen
import com.bmqa.brac.fitnesstracker.presentation.features.nutrition.ui.screens.NutritionDetailsScreen

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
                onNavigateToGeminiFoodAnalysis = { imageUri, selectedDate ->
                    navController.navigateToGeminiFoodAnalysis(imageUri = imageUri, selectedDate = selectedDate)
                },
                onNavigateToNutrition = { analysis ->
                    navController.navigateToNutritionWithAnalysis(analysis)
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
                preSelectedImageUri = imageUri,
                selectedDate = args.selectedDate
            )
        }

        composable<Route.Nutrition> {
            val args = it.toRoute<Route.Nutrition>()
            val geminiAnalysis = args.geminiAnalysis?.let { analysisId ->
                NavigationDataManager.getAnalysis(analysisId)
            }
            
            NutritionDetailsScreen(
                geminiAnalysis = geminiAnalysis,
                onNavigateBack = {
                    navController.navigateBack()
                }
            )
        }


    }
}
