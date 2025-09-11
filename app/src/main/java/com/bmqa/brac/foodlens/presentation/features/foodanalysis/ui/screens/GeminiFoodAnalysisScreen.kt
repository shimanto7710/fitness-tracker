package com.bmqa.brac.foodlens.presentation.features.foodanalysis.ui.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.bmqa.brac.foodlens.common.constants.AppConstants
import com.bmqa.brac.foodlens.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.foodlens.domain.entities.GeminiFoodItem
import com.bmqa.brac.foodlens.domain.entities.HealthStatus
import com.bmqa.brac.foodlens.presentation.components.ImagePicker
import com.bmqa.brac.foodlens.presentation.features.foodanalysis.viewmodel.GeminiFoodAnalysisViewModel
import com.bmqa.brac.foodlens.presentation.features.foodanalysis.viewmodel.GeminiFoodAnalysisUiState
import com.bmqa.brac.foodlens.ui.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

private object GeminiAnalysisConstants {
    const val IMAGE_HEIGHT = 300
    const val CARD_ELEVATION = 8
    const val BUTTON_HEIGHT = 56
    const val ICON_SIZE = 24
    const val BORDER_WIDTH = 1
}

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

    GeminiContent(
        modifier = modifier,
        selectedImageUri = selectedImageUri,
        onImageSelected = { uri ->
            selectedImageUri = uri
            viewModel.resetState() // Reset state when new image is selected
        },
        onAnalyzeFood = { uri ->
            viewModel.analyzeFoodWithGemini(uri, context, selectedDate)
        },
        onNavigateToNutritionDetails = onNavigateToNutritionDetails,
        onNavigateBack = onNavigateBack,
        selectedDate = selectedDate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiContent(
    modifier: Modifier = Modifier,
    selectedImageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    onAnalyzeFood: (Uri) -> Unit,
    onNavigateToNutritionDetails: (GeminiFoodAnalysis) -> Unit = {},
    onNavigateBack: () -> Unit,
    selectedDate: String? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Food Analysis",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
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
            onImageSelected = onImageSelected,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimensions.spacingLarge))

        // Selected Image Display
        selectedImageUri?.let { uri ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(GeminiAnalysisConstants.IMAGE_HEIGHT.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = GeminiAnalysisConstants.CARD_ELEVATION.dp),
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
                    onAnalyzeFood(uri)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(GeminiAnalysisConstants.BUTTON_HEIGHT.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(Dimensions.borderRadiusMedium)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = AppConstants.UiText.CONTENT_DESC_ANALYZE,
                    modifier = Modifier.size(GeminiAnalysisConstants.ICON_SIZE.dp)
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
                    onNavigateToNutritionDetails = onNavigateToNutritionDetails,
                    selectedDate = selectedDate
                )
            }
        }
    }
}


@Composable
private fun GeminiAnalysisResultsSection(
    selectedImageUri: Uri,
    onNavigateToNutritionDetails: (GeminiFoodAnalysis) -> Unit = {},
    selectedDate: String? = null
) {
    val viewModel: GeminiFoodAnalysisViewModel = koinViewModel()
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

                        // Food items list - Currently disabled for cleaner UI
                        // TODO: Re-enable if detailed food items display is needed

                        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))

                        // New Analysis button
                        OutlinedButton(
                            onClick = {
                                viewModel.analyzeFoodWithGemini(
                                    selectedImageUri,
                                    context,
                                    selectedDate
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(
                                GeminiAnalysisConstants.BORDER_WIDTH.dp,
                                MaterialTheme.colorScheme.primary
                            ),
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


@Preview(showBackground = true, name = "GeminiContent - With Image")
@Composable
private fun GeminiContentPreview_WithImage() {
    MaterialTheme {
        GeminiContent(
            selectedImageUri = null, // Will show image picker
            onImageSelected = {},
            onAnalyzeFood = {},
            onNavigateToNutritionDetails = {},
            onNavigateBack = {},
            selectedDate = "2024-01-15"
        )
    }
}