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
    onNavigateToGeminiFoodAnalysis: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Update app bar title and hide back button
    LaunchedEffect(Unit) {
        AppBarState.updateTitle(AppConstants.UiText.NAVIGATION_TITLE)
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
            text = AppConstants.UiText.CHOOSE_YOUR_FEATURE,
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Dimensions.spacingExtraLarge)
        )
        
        // Feature Cards
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium)
        ) {
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
                            text = AppConstants.UiText.ANALYZE_FOOD_IMAGES_FOR_CALORIES,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = AppConstants.UiText.CONTENT_DESC_GALLERY,
                        modifier = Modifier.size(Dimensions.iconSizeMedium),
                        tint = MaterialTheme.colorScheme.primary
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
                        contentDescription = AppConstants.UiText.CONTENT_DESC_GEMINI_FOOD_ANALYSIS,
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
                            text = AppConstants.UiText.ADVANCED_AI_FOOD_ANALYSIS,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = AppConstants.UiText.CONTENT_DESC_NAVIGATE,
                        modifier = Modifier.size(Dimensions.iconSizeMedium),
                        tint = MaterialTheme.colorScheme.error
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
