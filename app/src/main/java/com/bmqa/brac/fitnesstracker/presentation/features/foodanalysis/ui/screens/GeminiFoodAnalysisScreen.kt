package com.bmqa.brac.fitnesstracker.presentation.features.foodanalysis.ui.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import com.bmqa.brac.fitnesstracker.presentation.components.ImagePicker
import com.bmqa.brac.fitnesstracker.presentation.features.foodanalysis.viewmodel.GeminiFoodAnalysisViewModel
import com.bmqa.brac.fitnesstracker.presentation.features.foodanalysis.viewmodel.GeminiFoodAnalysisUiState
import com.bmqa.brac.fitnesstracker.ui.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun GeminiFoodAnalysisScreen(
    onNavigateBack: () -> Unit,
    onNavigateToNutritionDetails: (GeminiFoodAnalysis) -> Unit = {},
    preSelectedImageUri: Uri? = null,
    selectedDate: String? = null,
    modifier: Modifier = Modifier
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(preSelectedImageUri) }
    val viewModel: GeminiFoodAnalysisViewModel = koinViewModel()
    val context = LocalContext.current
    
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimensions.spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = AppConstants.UiText.AI_POWERED_FOOD_ANALYSIS,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = Dimensions.spacingMedium)
        )
        
        Text(
            text = AppConstants.UiText.TAKE_PHOTO_OR_SELECT_GALLERY_GEMINI,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = Dimensions.spacingLarge),
            textAlign = TextAlign.Center
        )
        
        // Image Picker Component
        ImagePicker(
            onImageSelected = { uri ->
                selectedImageUri = uri
                viewModel.resetState() // Reset state when new image is selected
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
        
        // Selected Image Display
        selectedImageUri?.let { uri ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(Dimensions.borderRadiusMedium)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = uri,
                        contentDescription = AppConstants.UiText.CONTENT_DESC_SELECTED_FOOD_IMAGE,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
            
            // Analyze Button - Only visible when image is selected
            Button(
                onClick = { 
                    viewModel.analyzeFoodWithGemini(uri, context, selectedDate)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(Dimensions.borderRadiusMedium)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = AppConstants.UiText.CONTENT_DESC_ANALYZE,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
                Text(
                    text = AppConstants.UiText.ANALYZE_WITH_GEMINI_AI,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
            
            // Analysis Results Section
            GeminiAnalysisResultsSection(
                selectedImageUri = uri,
                viewModel = viewModel,
                onNavigateToNutritionDetails = onNavigateToNutritionDetails,
                selectedDate = selectedDate
            )
        }
    }
}

@Composable
private fun GeminiAnalysisResultsSection(
    selectedImageUri: Uri,
    viewModel: GeminiFoodAnalysisViewModel,
    onNavigateToNutritionDetails: (GeminiFoodAnalysis) -> Unit = {},
    selectedDate: String? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    // Automatically navigate to nutrition details when analysis is successful
    LaunchedEffect(uiState) {
        if (uiState is GeminiFoodAnalysisUiState.Success) {
            val foodAnalysis = (uiState as GeminiFoodAnalysisUiState.Success).foodAnalysis
            if (!foodAnalysis.isError) {
                onNavigateToNutritionDetails(foodAnalysis)
            }
        }
    }
    
    when {
        uiState is GeminiFoodAnalysisUiState.Loading -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(Dimensions.spacingMedium),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                    Text(
                        text = AppConstants.UiText.ANALYZING_FOOD_WITH_GEMINI,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                    Text(
                        text = AppConstants.UiText.THIS_MAY_TAKE_FEW_MOMENTS,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        uiState is GeminiFoodAnalysisUiState.Error -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(Dimensions.spacingMedium)
                ) {
                    Text(
                        text = AppConstants.UiText.ANALYSIS_ERROR,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    
                    Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                    
                    Text(
                        text = (uiState as GeminiFoodAnalysisUiState.Error).message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    
                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                    
                    Button(
                        onClick = { 
                            viewModel.analyzeFoodWithGemini(selectedImageUri, context, selectedDate)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(Dimensions.borderRadiusSmall)
                    ) {
                        Text(AppConstants.UiText.RETRY_ANALYSIS)
                    }
                }
            }
        }
        
        uiState is GeminiFoodAnalysisUiState.Success -> {
            val foodAnalysis = (uiState as GeminiFoodAnalysisUiState.Success).foodAnalysis
            
            if (foodAnalysis.isError) {
                // Show error message for non-food images
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.spacingMedium),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = AppConstants.UiText.NOT_A_FOOD_IMAGE,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                        
                        Text(
                            text = foodAnalysis.errorMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                        
                        Button(
                            onClick = { 
                                viewModel.resetState()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            shape = RoundedCornerShape(Dimensions.borderRadiusSmall)
                        ) {
                            Text(AppConstants.UiText.TRY_DIFFERENT_IMAGE)
                        }
                    }
                }
            } else {
                // Show successful food analysis
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.spacingMedium)
                    ) {
                        Text(
                            text = AppConstants.UiText.GEMINI_AI_ANALYSIS_RESULTS,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                        
                        Text(
                            text = foodAnalysis.analysisSummary,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                        
                        // Food items list
                        /*foodAnalysis.foodItems.forEach { foodItem ->
                            GeminiFoodItemResult(foodItem = foodItem)
                            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                        }*/
                        
                        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                        
                        // New Analysis button
                        OutlinedButton(
                            onClick = { 
                                viewModel.analyzeFoodWithGemini(selectedImageUri, context, selectedDate)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(Dimensions.borderRadiusSmall)
                        ) {
                            Text(AppConstants.UiText.ANALYZE_AGAIN)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GeminiFoodItemResult(
    foodItem: GeminiFoodItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spacingMedium)
        ) {
            // Food name and health status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = foodItem.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                HealthStatusChip(healthStatus = foodItem.healthStatus)
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
            
            // Portion and digestion time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = AppConstants.UiText.LABEL_PORTION,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = foodItem.portion,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = AppConstants.UiText.LABEL_DIGESTION,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = foodItem.digestionTime,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
            
            // Nutrition information
            if (foodItem.calories != null || foodItem.protein != null || foodItem.carbs != null || foodItem.fat != null) {
                Text(
                    text = AppConstants.UiText.LABEL_NUTRITION_INFORMATION,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    foodItem.calories?.let { calories ->
                        NutritionInfo(label = AppConstants.UiText.CALORIES.replace(": %d", ""), value = "$calories")
                    }
                    foodItem.protein?.let { protein ->
                        NutritionInfo(label = AppConstants.UiText.PROTEIN.replace(": %s", ""), value = protein)
                    }
                    foodItem.carbs?.let { carbs ->
                        NutritionInfo(label = AppConstants.UiText.CARBS.replace(": %s", ""), value = carbs)
                    }
                    foodItem.fat?.let { fat ->
                        NutritionInfo(label = AppConstants.UiText.FAT.replace(": %s", ""), value = fat)
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
            }
            
            // Health benefits
            if (foodItem.healthBenefits.isNotEmpty()) {
                Text(
                    text = AppConstants.UiText.LABEL_HEALTH_BENEFITS,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                
                foodItem.healthBenefits.forEach { benefit ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
                        Text(
                            text = benefit,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                }
                
                Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
            }
            
            // Health concerns
            if (foodItem.healthConcerns.isNotEmpty()) {
                Text(
                    text = AppConstants.UiText.LABEL_HEALTH_CONCERNS,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                
                foodItem.healthConcerns.forEach { concern ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
                        Text(
                            text = concern,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                }
            }
        }
    }
}

@Composable
private fun HealthStatusChip(
    healthStatus: HealthStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (healthStatus) {
        HealthStatus.EXCELLENT -> MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
        HealthStatus.GOOD -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
        HealthStatus.MODERATE -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
        HealthStatus.POOR -> MaterialTheme.colorScheme.error to MaterialTheme.colorScheme.onError
        HealthStatus.UNKNOWN -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = healthStatus.name,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun NutritionInfo(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
