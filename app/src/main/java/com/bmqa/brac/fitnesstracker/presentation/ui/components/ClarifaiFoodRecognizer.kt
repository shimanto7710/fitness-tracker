package com.bmqa.brac.fitnesstracker.presentation.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.presentation.viewmodel.FoodRecognitionViewModel
import com.bmqa.brac.fitnesstracker.ui.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun ClarifaiFoodRecognizer(
    selectedImageUri: Uri?,
    viewModel: FoodRecognitionViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    

    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.spacingMedium)
    ) {

        
        // Image display
        if (selectedImageUri != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.imageHeight)
                    .padding(vertical = Dimensions.spacingSmall)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Background image
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = AppConstants.UiText.IMAGE_PICKER_TITLE,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            // Analyze button
            if (!uiState.isLoading && uiState.foodItems.isEmpty() && uiState.error == null) {
                Button(
                    onClick = { 
                        viewModel.recognizeFoodFromImage(selectedImageUri, context)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(Dimensions.borderRadiusMedium)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Analyze",
                        modifier = Modifier.size(Dimensions.iconSizeSmall)
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
                    Text(
                        text = AppConstants.UiText.ANALYZE_FOOD,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Loading state
            if (uiState.isLoading) {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(Dimensions.borderRadiusMedium)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Dimensions.iconSizeSmall),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
                    Text(AppConstants.UiText.ANALYZING_FOOD)
                }
            }
            
            // Error display
            uiState.error?.let { error ->
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
                            text = "${AppConstants.UiText.ERROR}: $error",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spacingSmall))
                        
                        // Retry button
                        Button(
                            onClick = { 
                                viewModel.recognizeFoodFromImage(selectedImageUri, context)
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
            
            // Detection results
            if (uiState.foodItems.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppConstants.UiText.DETECTED_FOODS.format(uiState.foodItems.size),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    
                    // New Analysis button
                    OutlinedButton(
                        onClick = { 
                            viewModel.recognizeFoodFromImage(selectedImageUri, context)
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("New Analysis")
                    }
                }
                
                Spacer(modifier = Modifier.height(AppConstants.Dimensions.PADDING_SMALL.dp))
                
                // Use a fixed height container for the LazyColumn to avoid infinite constraints
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppConstants.Dimensions.FOOD_LIST_HEIGHT.dp) // Fixed height to avoid infinite constraints
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(AppConstants.Dimensions.PADDING_SMALL.dp)
                    ) {
                        items(uiState.foodItems) { food ->
                            FoodItemCard(
                                food = food,
                                isSelected = uiState.selectedFood == food,
                                onClick = { viewModel.selectFood(food) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(
    food: FoodItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(AppConstants.Dimensions.PADDING_MEDIUM.dp)
        ) {
            Text(
                text = food.name,
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = AppConstants.UiText.CONFIDENCE.format((food.confidence * 100).toInt()),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
