package com.bmqa.brac.fitnesstracker.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import com.bmqa.brac.fitnesstracker.presentation.ui.utils.AssetImageUtils
import java.io.File
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bmqa.brac.fitnesstracker.R
import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import com.bmqa.brac.fitnesstracker.presentation.state.AppBarState
import com.bmqa.brac.fitnesstracker.presentation.viewmodel.GeminiFoodAnalysisViewModel
import com.bmqa.brac.fitnesstracker.ui.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun NutritionDetailsScreen(
    geminiAnalysis: GeminiFoodAnalysis? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GeminiFoodAnalysisViewModel = koinViewModel()
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

    NutritionDetailsContent(
        foodItem = foodItem,
        onNavigateBack = onNavigateBack
    )

}

@Composable
private fun NutritionDetailsContent(
    foodItem: GeminiFoodItem?,
    onNavigateBack: () -> Unit,
) {
    Scaffold { paddingValues ->
        Surface {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color.White)
                // Remove or change contentAlignment
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // image section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    ) {
                        AssetOrDrawableImage(
                            assetFileName = "food_plate.jpg",
                            drawableRes = R.drawable.food_plate,
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = "Meal Image"
                        )
                    }

                    Card(
                        modifier = Modifier
                            .offset(y = (-24).dp)
                            .fillMaxSize()
                            .background(Color.White),
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Column {
                                    // title and time
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

                                    Spacer(modifier = Modifier.height(16.dp))
                                    // total Calories
                                    CaloriesCard()


                                    Spacer(modifier = Modifier.height(16.dp))
//                                    calorie details
                                    CalorieDetails()

                                    Spacer(modifier = Modifier.height(16.dp))

//                                    health info
                                    HealthScoreCard(
                                        title = "Health Score",
                                        value = "7/10",
                                        progress = 0.7f,
                                        icon = Icons.Filled.Favorite
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Text(
                                        "Ingredients",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    FoodItemDetails()

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CaloriesCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxSize()
        ) {
            // Icon box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Calories Icon",
                    tint = Color.Black
                )
            }

            Column(
                modifier = Modifier.padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Calories", fontSize = 10.sp)
                Text(
                    "621",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
private fun CalorieDetails() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp) // space between cards
    ) {
        CalorieCard(label = "Protein", value = "621", modifier = Modifier.weight(1f))
        CalorieCard(label = "Carb", value = "52g", modifier = Modifier.weight(1f))
        CalorieCard(label = "Fats", value = "20g", modifier = Modifier.weight(1f))
    }
}

@Composable
fun CalorieCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth() // slightly taller for balance
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {



        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(start = 8.dp)) {
                Text(label, fontSize = 12.sp)
                Text(
                    value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}



@Composable
fun HealthScoreCard(
    title: String,
    value: String,
    progress: Float,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Content
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(title, fontSize = 12.sp)
                    Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = progress.coerceIn(0f, 1f), // clamp between 0 and 1
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF4CAF50),
                    trackColor = Color(0xFFE0E0E0)
                )
            }
        }
    }
}

// Data class for food items
data class FoodItemData(
    val name: String,
    val calories: String,
    val description: String,
    val protein: String,
    val carbs: String,
    val fat: String
)

@Composable
fun FoodItemDetails(){
    val foodItems = listOf(
        FoodItemData(
            name = "Salad with eggs",
            calories = "294 Kcal-100gm",
            description = "A salad with boiled eggs, mixed greens, cherry tomatoes, cucumbers, and a light vinaigrette dressing. This dish is rich in protein from the eggs and packed with vitamins and minerals from the fresh vegetables.",
            protein = "12g",
            carbs = "8g",
            fat = "5g"
        ),
        FoodItemData(
            name = "Grilled Chicken Breast",
            calories = "165 Kcal-100gm",
            description = "Lean grilled chicken breast seasoned with herbs and spices. High in protein and low in fat, perfect for muscle building and weight management.",
            protein = "31g",
            carbs = "0g",
            fat = "3.6g"
        ),
        FoodItemData(
            name = "Quinoa Bowl",
            calories = "120 Kcal-100gm",
            description = "Nutritious quinoa mixed with vegetables, chickpeas, and a tahini dressing. A complete protein source with fiber and essential nutrients.",
            protein = "4.4g",
            carbs = "22g",
            fat = "1.9g"
        )
    )
    
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(foodItems) { foodItem ->
            FoodItem(
                name = foodItem.name,
                calories = foodItem.calories,
                description = foodItem.description,
                protein = foodItem.protein,
                carbs = foodItem.carbs,
                fat = foodItem.fat
            )
        }
    }
}

@Composable
fun FoodItem(
    name: String,
    calories: String,
    description: String,
    protein: String,
    carbs: String,
    fat: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = calories,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp)
                ) {
                    Text(
                        protein, 
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Protein",
                        fontSize = 12.sp
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp)
                ) {
                    Text(
                        carbs, 
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Carbs",
                        fontSize = 12.sp
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp)
                ) {
                    Text(
                        fat, 
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Fat",
                        fontSize = 12.sp
                    )
                }
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

@Composable
fun AssetOrDrawableImage(
    assetFileName: String,
    drawableRes: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String? = null
) {
    val context = LocalContext.current

    if (LocalInspectionMode.current) {
        // Preview mode → use drawable
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        // Runtime mode → load from assets
        AsyncImage(
            model = "file:///android_asset/$assetFileName",
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun NutritionDetailsContent_Preview() {
    // Use the drawable version of food_plate for preview
    val mockData = createMockGeminiAnalysis()

    NutritionDetailsContent(
        foodItem = mockData.foodItems.firstOrNull(),
        onNavigateBack = {}
    )
}

