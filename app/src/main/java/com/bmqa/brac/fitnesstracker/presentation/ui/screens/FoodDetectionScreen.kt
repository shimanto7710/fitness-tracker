package com.bmqa.brac.fitnesstracker.presentation.ui.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.presentation.state.AppBarState
import com.bmqa.brac.fitnesstracker.presentation.ui.components.ImagePicker
import com.bmqa.brac.fitnesstracker.presentation.viewmodel.CustomFoodDetectionViewModel
import com.bmqa.brac.fitnesstracker.presentation.viewmodel.CustomFoodDetectionUiState
import com.bmqa.brac.fitnesstracker.ui.theme.Dimensions

@Composable
fun FoodDetectionScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val viewModel: CustomFoodDetectionViewModel = hiltViewModel()
    val context = LocalContext.current
    
    // Update app bar title and show back button
    LaunchedEffect(Unit) {
        AppBarState.updateTitle("Food Detection")
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
            text = "Food Detection",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = Dimensions.spacingMedium)
        )
        
        Text(
            text = "Take a photo or select from gallery to analyze food",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = Dimensions.spacingLarge),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
                    viewModel.detectFoodFromImage(uri, context)
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
                    text = "Analyze Food",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
            
            // Analysis Results Section
            AnalysisResultsSection(
                selectedImageUri = uri,
                viewModel = viewModel,
                context = context
            )
        }
    }
}

@Composable
private fun AnalysisResultsSection(
    selectedImageUri: Uri,
    viewModel: CustomFoodDetectionViewModel,
    context: Context
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    when {
        uiState is CustomFoodDetectionUiState.Loading -> {
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
                        text = "Analyzing food image...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        uiState is CustomFoodDetectionUiState.Error -> {
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
                        text = "Error",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    
                    Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                    
                    Text(
                        text = (uiState as CustomFoodDetectionUiState.Error).message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    
                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                    
                    Button(
                        onClick = { 
                            viewModel.detectFoodFromImage(selectedImageUri, context)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(Dimensions.borderRadiusSmall)
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
        
        uiState is CustomFoodDetectionUiState.Success -> {
            val foodItems = (uiState as CustomFoodDetectionUiState.Success).foodItems
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
                        text = "Detection Results",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                    
                    Text(
                        text = "Found ${foodItems.size} food item(s)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                    
                    // Food items list
                    foodItems.forEach { foodItem ->
                        FoodItemResult(foodItem = foodItem)
                        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                    }
                    
                    Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
                    
                    // New Analysis button
                    OutlinedButton(
                        onClick = { 
                            viewModel.detectFoodFromImage(selectedImageUri, context)
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

@Composable
private fun FoodItemResult(
    foodItem: com.bmqa.brac.fitnesstracker.domain.entities.FoodItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(Dimensions.spacingMedium)
        ) {
            Text(
                text = foodItem.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
            
            Text(
                text = "Confidence: ${(foodItem.confidence * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Show bounding box coordinates if available
            foodItem.boundingBox?.let { bbox ->
                Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                Text(
                    text = "Location: x=${bbox.x}, y=${bbox.y}, w=${bbox.width}, h=${bbox.height}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Note: This API doesn't provide nutrition info, so we show a message
            Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
            Text(
                text = "Nutrition info not available from this API",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}
