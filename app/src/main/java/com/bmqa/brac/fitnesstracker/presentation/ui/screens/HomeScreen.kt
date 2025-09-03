package com.bmqa.brac.fitnesstracker.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.presentation.state.AppBarState
import com.bmqa.brac.fitnesstracker.ui.theme.Dimensions

@Composable
fun HomeScreen(
    onNavigateToCaloriesManagement: () -> Unit,
    onNavigateToFoodDetection: () -> Unit,
    onNavigateToImageCapture: () -> Unit,
    onNavigateToGeminiFoodAnalysis: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Update app bar title and hide back button
    LaunchedEffect(Unit) {
        AppBarState.updateTitle("Fitness Tracker")
        AppBarState.hideBackButton()
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimensions.spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose your feature",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Dimensions.spacingExtraLarge)
        )
        
        // Feature Cards
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium)
        ) {
            // Food Detection Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.cardHeight),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                onClick = onNavigateToFoodDetection
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimensions.spacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Food Detection",
                        modifier = Modifier.size(Dimensions.iconSizeExtraLarge),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spacingMedium))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Food Detection",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                        Text(
                            text = "Take photos and analyze food for nutrition",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Navigate",
                        modifier = Modifier.size(Dimensions.iconSizeMedium),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            
            // Calories Management Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.cardHeight),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                onClick = onNavigateToCaloriesManagement
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimensions.spacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = AppConstants.UiText.IMAGE_PICKER_TITLE,
                        modifier = Modifier.size(Dimensions.iconSizeExtraLarge),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spacingMedium))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = AppConstants.UiText.CALORIES_MANAGEMENT_TITLE,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                        Text(
                            text = "Analyze food images for calorie tracking",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Gallery",
                        modifier = Modifier.size(Dimensions.iconSizeMedium),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            // AI Food Analysis Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.cardHeight),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                onClick = onNavigateToImageCapture
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimensions.spacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "AI Food Analysis",
                        modifier = Modifier.size(Dimensions.iconSizeExtraLarge),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spacingMedium))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = AppConstants.UiText.IMAGE_CAPTURE_TITLE,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                        Text(
                            text = "AI-powered food analysis with advanced models",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Navigate",
                        modifier = Modifier.size(Dimensions.iconSizeMedium),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            
            // Gemini Food Analysis Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.cardHeight),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                onClick = onNavigateToGeminiFoodAnalysis
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimensions.spacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Gemini Food Analysis",
                        modifier = Modifier.size(Dimensions.iconSizeExtraLarge),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spacingMedium))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = AppConstants.UiText.GEMINI_FOOD_ANALYSIS_TITLE,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                        Text(
                            text = "Advanced AI food analysis with Google Gemini",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Navigate",
                        modifier = Modifier.size(Dimensions.iconSizeMedium),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            // Fitness Tracking Card (Placeholder for future features)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.cardHeight),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimensions.spacingMedium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Fitness Tracking",
                        modifier = Modifier.size(Dimensions.iconSizeExtraLarge),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spacingMedium))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Fitness Tracking",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                        Text(
                            text = "Track workouts and progress",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                        )
                    }
                    Text(
                        text = "Coming Soon",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(Dimensions.borderRadiusSmall))
                            .padding(horizontal = Dimensions.spacingSmall, vertical = 4.dp)
                    )
                }
            }
        }
        
        /*Spacer(modifier = Modifier.weight(1f))
        
        // Action Button
        Button(
            onClick = onNavigateToCaloriesManagement,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Start",
                modifier = Modifier.size(AppConstants.Dimensions.ICON_SIZE_MEDIUM.dp)
            )
            Spacer(modifier = Modifier.width(AppConstants.Dimensions.PADDING_SMALL.dp))
            Text(
                text = "Start Image Picker",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }*/
    }
}
