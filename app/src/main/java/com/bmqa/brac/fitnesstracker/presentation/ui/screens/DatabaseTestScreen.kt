package com.bmqa.brac.fitnesstracker.presentation.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bmqa.brac.fitnesstracker.data.local.repository.LocalFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import com.bmqa.brac.fitnesstracker.domain.entities.TotalNutrition
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun DatabaseTestScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { LocalFoodAnalysisRepository(context) }
    val scope = rememberCoroutineScope()
    
    var savedAnalyses by remember { mutableStateOf<List<GeminiFoodAnalysis>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    // Load saved analyses on screen load
    LaunchedEffect(Unit) {
        repository.getAllFoodAnalyses().collect { analyses ->
            savedAnalyses = analyses
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Database Test Screen",
            style = MaterialTheme.typography.headlineMedium
        )

        // Test buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {
                            val testAnalysis = createTestFoodAnalysis()
                            val testBitmap = createTestBitmap()
                            
                            val analysisId = repository.saveFoodAnalysis(
                                foodAnalysis = testAnalysis,
                                imageUri = "test://image/uri",
                                imageBitmap = testBitmap
                            )
                            
                            message = "✅ Saved analysis with ID: $analysisId"
                        } catch (e: Exception) {
                            message = "❌ Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text("Save Test Analysis")
            }

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {
                            val recentAnalyses = repository.getRecentFoodAnalyses(limit = 5).first()
                            message = "📋 Found ${recentAnalyses.size} recent analyses"
                        } catch (e: Exception) {
                            message = "❌ Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text("Get Recent")
            }

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        try {
                            if (savedAnalyses.isNotEmpty()) {
                                val firstAnalysis = savedAnalyses.first()
                                message = "🔍 First analysis: ${firstAnalysis.totalNutrition?.name}"
                            } else {
                                message = "📭 No analyses found"
                            }
                        } catch (e: Exception) {
                            message = "❌ Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading && savedAnalyses.isNotEmpty()
            ) {
                Text("Test Get")
            }
        }

        // Status message
        if (message.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (message.startsWith("✅")) 
                        MaterialTheme.colorScheme.primaryContainer 
                    else if (message.startsWith("❌")) 
                        MaterialTheme.colorScheme.errorContainer
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Loading indicator
        if (isLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.width(8.dp))
                Text("Loading...")
            }
        }

        // Saved analyses list
        Text(
            text = "Saved Analyses (${savedAnalyses.size})",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(savedAnalyses) { analysis ->
                AnalysisCard(analysis = analysis)
            }
        }
    }
}

@Composable
private fun AnalysisCard(
    analysis: GeminiFoodAnalysis,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = analysis.totalNutrition?.name ?: "Unknown Meal",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Calories: ${analysis.totalNutrition?.totalCalories ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Items: ${analysis.foodItems.size}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Date: ${analysis.dateNTime ?: "Unknown"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (analysis.analysisSummary.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = analysis.analysisSummary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }
        }
    }
}

// Helper functions for testing
private fun createTestFoodAnalysis(): GeminiFoodAnalysis {
    return GeminiFoodAnalysis(
        isError = false,
        errorMessage = "",
        foodItems = listOf(
            GeminiFoodItem(
                name = "Test Grilled Chicken",
                portion = "150g",
                digestionTime = "3-4 hours",
                healthStatus = HealthStatus.GOOD,
                calories = 250,
                protein = "25g",
                carbs = "0g",
                fat = "15g",
                healthBenefits = listOf("High protein", "Low carb"),
                healthConcerns = listOf("None"),
                analysisSummary = "Healthy protein source"
            ),
            GeminiFoodItem(
                name = "Test Brown Rice",
                portion = "100g",
                digestionTime = "2-3 hours",
                healthStatus = HealthStatus.GOOD,
                calories = 110,
                protein = "2g",
                carbs = "23g",
                fat = "1g",
                healthBenefits = listOf("Complex carbs", "Fiber"),
                healthConcerns = listOf("None"),
                analysisSummary = "Good carbohydrate source"
            )
        ),
        totalNutrition = TotalNutrition(
            name = "Test Chicken & Rice",
            totalPortion = "250g",
            totalCalories = 360,
            totalProtein = "27g",
            totalCarbs = "23g",
            totalFat = "16g",
            overallHealthStatus = HealthStatus.GOOD
        ),
        analysisSummary = "A balanced meal with good protein and carbohydrate sources. Healthy choice for muscle building and energy.",
        dateNTime = "2024-01-15 12:30 PM"
    )
}

private fun createTestBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.BLUE)
    return bitmap
}
