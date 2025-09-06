package com.bmqa.brac.fitnesstracker.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bmqa.brac.fitnesstracker.R
import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import com.bmqa.brac.fitnesstracker.presentation.state.AppBarState
import com.bmqa.brac.fitnesstracker.ui.theme.Dimensions

@Composable
fun NutritionDetailsScreen(
    geminiAnalysis: GeminiFoodAnalysis? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Update app bar state
    LaunchedEffect(Unit) {
        AppBarState.updateTitle(AppConstants.UiText.NUTRITION_TITLE)
        AppBarState.showBackButton(onNavigateBack)
        AppBarState.setTransparentMode(false)
    }

    // Get analysis data (provided or mock)
    val analysis = geminiAnalysis ?: createMockGeminiAnalysis()
    val foodItem = analysis.foodItems.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400&h=300&fit=crop",
                contentDescription = "Meal Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Top Bar
            TopBar(onNavigateBack)
        }
        NutritionDetailsContent(
            foodItem = foodItem,
            onNavigateBack = onNavigateBack
        )
    }
}

/**
 * Main content composable for nutrition details
 */
@Composable
private fun NutritionDetailsContent(
    foodItem: GeminiFoodItem?,
    onNavigateBack: () -> Unit,
) {
    Card(
/*        modifier = Modifier
            .offset(y = (-24).dp),
//            .background(Color.White),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
        )*/
    ) {
        Column(Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .heightIn(min = 20.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8F8F8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "12:46 PM",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        foodItem?.name ?: "Unknown Food",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
private fun TopBar(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0x80FFFFFF))
                    .padding(4.dp)
            )
        }

        Row {
            IconButton(onClick = { /* share */ }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0x80FFFFFF))
                        .padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { /* menu */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0x80FFFFFF))
                        .padding(4.dp)
                )
            }
        }
    }
}

private fun createMockGeminiAnalysis(): GeminiFoodAnalysis {
    return GeminiFoodAnalysis(
        isError = false,
        errorMessage = "",
        foodItems = listOf(
            GeminiFoodItem(
                name = "Salmon and Broccoli Tray Bake",
                portion = "1 serving",
                digestionTime = "2-3 hours",
                healthStatus = HealthStatus.GOOD,
                calories = 621,
                protein = "52g",
                carbs = "20g",
                fat = "36g",
                healthBenefits = listOf(
                    "High in Omega-3 fatty acids",
                    "Rich in protein",
                    "Contains antioxidants",
                    "Good source of vitamins"
                ),
                healthConcerns = listOf(
                    "High in sodium",
                    "Contains allergens"
                )
            )
        ),
        analysisSummary = "This is a nutritious meal with good protein content and healthy fats from salmon, combined with fiber-rich broccoli. The meal provides essential nutrients but should be consumed in moderation due to its calorie content."
    )
}
