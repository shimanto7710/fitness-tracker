package com.bmqa.brac.fitnesstracker.presentation.ui.screens

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import com.bmqa.brac.fitnesstracker.presentation.state.AppBarState
import com.bmqa.brac.fitnesstracker.presentation.ui.components.ImagePicker
import com.bmqa.brac.fitnesstracker.presentation.viewmodel.GeminiFoodAnalysisViewModel
import com.bmqa.brac.fitnesstracker.presentation.viewmodel.GeminiFoodAnalysisUiState
import com.bmqa.brac.fitnesstracker.ui.theme.Dimensions

@Composable
fun GeminiFoodAnalysisScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val viewModel: GeminiFoodAnalysisViewModel = hiltViewModel()
    val context = LocalContext.current
    
    // Update app bar title and show back button
    LaunchedEffect(Unit) {
        AppBarState.updateTitle("Gemini Food Analysis")
        AppBarState.showBackButton(onNavigateBack)
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimensions.spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "AI-Powered Food Analysis",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = Dimensions.spacingMedium)
        )
        
        Text(
            text = "Take a photo or select from gallery to get detailed food analysis using Gemini AI",
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
                        contentDescription = "Selected food image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
            
            // Analyze Button - Only visible when image is selected
            Button(
                onClick = { 
                    viewModel.analyzeFoodWithGemini(uri, context)
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
                    contentDescription = "Analyze",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
                Text(
                    text = "Analyze with Gemini AI",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
            
            // Analysis Results Section
            GeminiAnalysisResultsSection(
                selectedImageUri = uri,
                viewModel = viewModel,
                context = context
            )
        }
    }
}

@Composable
private fun GeminiAnalysisResultsSection(
    selectedImageUri: Uri,
    viewModel: GeminiFoodAnalysisViewModel,
    context: Context
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
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
                        text = "Analyzing food with Gemini AI...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                    Text(
                        text = "This may take a few moments",
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
                        text = "Analysis Error",
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
                            viewModel.analyzeFoodWithGemini(selectedImageUri, context)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(Dimensions.borderRadiusSmall)
                    ) {
                        Text("Retry Analysis")
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
                            text = "⚠️ Not a Food Image",
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
                            Text("Try Different Image")
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
                            text = "Gemini AI Analysis Results",
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
                        foodAnalysis.foodItems.forEach { foodItem ->
                            GeminiFoodItemResult(foodItem = foodItem)
                            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                        }
                        
                        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                        
                        // New Analysis button
                        OutlinedButton(
                            onClick = { 
                                viewModel.analyzeFoodWithGemini(selectedImageUri, context)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(Dimensions.borderRadiusSmall)
                        ) {
                            Text("Analyze Again")
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
                        text = "Portion:",
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
                        text = "Digestion:",
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
                    text = "Nutrition Information",
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
                        NutritionInfo(label = "Calories", value = "$calories")
                    }
                    foodItem.protein?.let { protein ->
                        NutritionInfo(label = "Protein", value = protein)
                    }
                    foodItem.carbs?.let { carbs ->
                        NutritionInfo(label = "Carbs", value = carbs)
                    }
                    foodItem.fat?.let { fat ->
                        NutritionInfo(label = "Fat", value = fat)
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
            }
            
            // Health benefits
            if (foodItem.healthBenefits.isNotEmpty()) {
                Text(
                    text = "Health Benefits",
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
                    text = "Health Concerns",
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
