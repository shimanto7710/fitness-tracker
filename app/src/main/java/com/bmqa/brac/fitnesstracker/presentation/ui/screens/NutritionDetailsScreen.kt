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
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.bmqa.brac.fitnesstracker.presentation.ui.utils.AssetImageUtils
import java.io.File
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.LayoutDirection
import coil.compose.AsyncImage
import com.bmqa.brac.fitnesstracker.R
import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import com.bmqa.brac.fitnesstracker.domain.entities.TotalNutrition
import com.bmqa.brac.fitnesstracker.presentation.viewmodel.GeminiFoodAnalysisViewModel
import com.bmqa.brac.fitnesstracker.ui.theme.Dimensions
import org.koin.androidx.compose.koinViewModel

@Composable
fun NutritionDetailsScreen(
    geminiAnalysis: GeminiFoodAnalysis? = null,
    onNavigateBack: () -> Unit,
    onTryAgain: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: GeminiFoodAnalysisViewModel = koinViewModel()
) {

    // Get analysis data (provided or mock)
    val analysis = geminiAnalysis ?: createMockGeminiAnalysis()

    NutritionDetailsContent(
        geminiAnalysis = analysis,
        onNavigateBack = onNavigateBack,
        onTryAgain = onTryAgain
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NutritionDetailsContent(
    geminiAnalysis: GeminiFoodAnalysis,
    onNavigateBack: () -> Unit,
    onTryAgain: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppConstants.UiText.NUTRITION_TITLE,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
        },
    ) { paddingValues ->
        Surface {
            LazyColumn(
                modifier = Modifier
                    .padding(
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        top = paddingValues.calculateTopPadding()
                    )
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                // Image section
                item {
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
                }

                // Content card
                item {
                    Card(
                        modifier = Modifier

                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Color.White),
                        elevation = CardDefaults.cardElevation(8.dp),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                        ),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                        // Title and time
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
                            geminiAnalysis.foodItems.firstOrNull()?.name ?: "Unknown Food",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        // Total Calories
                        CaloriesCard(
                            calories = geminiAnalysis.totalNutrition?.totalCalories
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        // Calorie details
                        CalorieDetails(
                            protein = geminiAnalysis.totalNutrition?.totalProtein,
                            carbs = geminiAnalysis.totalNutrition?.totalCarbs,
                            fat = geminiAnalysis.totalNutrition?.totalFat
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Health info
                        HealthScoreCard(
                            title = "Health Score",
                            value = when (geminiAnalysis.totalNutrition?.overallHealthStatus) {
                                HealthStatus.EXCELLENT -> "9/10"
                                HealthStatus.GOOD -> "7/10"
                                HealthStatus.MODERATE -> "5/10"
                                HealthStatus.POOR -> "3/10"
                                else -> "N/A"
                            },
                            progress = when (geminiAnalysis.totalNutrition?.overallHealthStatus) {
                                HealthStatus.EXCELLENT -> 0.9f
                                HealthStatus.GOOD -> 0.7f
                                HealthStatus.MODERATE -> 0.5f
                                HealthStatus.POOR -> 0.3f
                                else -> 0.0f
                            },
                            icon = Icons.Filled.Favorite
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "Food Items",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                         FoodItemDetails(
                             foodItems = geminiAnalysis.foodItems
                         )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CaloriesCard(
    calories: Int? = null
) {
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
                    calories?.toString() ?: "N/A",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
private fun CalorieDetails(
    protein: String? = null,
    carbs: String? = null,
    fat: String? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp) // space between cards
    ) {
        CalorieCard(label = "Protein", value = protein ?: "N/A", modifier = Modifier.weight(1f))
        CalorieCard(label = "Carb", value = carbs ?: "N/A", modifier = Modifier.weight(1f))
        CalorieCard(label = "Fats", value = fat ?: "N/A", modifier = Modifier.weight(1f))
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 8.dp)
            ) {
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
fun FoodItemDetails(
    foodItems: List<GeminiFoodItem>
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        foodItems.forEach { foodItem ->
            FoodItem(
                name = foodItem.name,
                calories = foodItem.calories?.toString() ?: "N/A",
                description = foodItem.analysisSummary,
                protein = foodItem.protein ?: "N/A",
                carbs = foodItem.carbs ?: "N/A",
                fat = foodItem.fat ?: "N/A"
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = calories + " kcal",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Black,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
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
                name = "Grilled Pork Chop",
                portion = "150g",
                digestionTime = "3-4 hours",
                healthStatus = HealthStatus.MODERATE,
                calories = 350,
                protein = "30g",
                carbs = "0g",
                fat = "25g",
                healthBenefits = listOf(
                    "Good source of protein",
                    "Provides essential amino acids"
                ),
                healthConcerns = listOf(
                    "High in saturated fat",
                    "May contribute to elevated cholesterol levels if consumed in excess",
                    "Risk of heterocyclic amines (HCAs) and polycyclic aromatic hydrocarbons (PAHs) formation during grilling"
                ),
                analysisSummary = "A good source of protein but also high in fat. Moderation is key."
            ),
            GeminiFoodItem(
                name = "Boiled Potatoes with Dill",
                portion = "200g",
                digestionTime = "2-3 hours",
                healthStatus = HealthStatus.GOOD,
                calories = 160,
                protein = "3g",
                carbs = "37g",
                fat = "0g",
                healthBenefits = listOf(
                    "Good source of potassium",
                    "Provides carbohydrates for energy",
                    "Contains vitamin C and B6"
                ),
                healthConcerns = listOf(
                    "High glycemic index, may cause blood sugar spikes",
                    "Can be calorie-dense if consumed in large quantities"
                ),
                analysisSummary = "A healthy source of carbohydrates and essential nutrients."
            ),
            GeminiFoodItem(
                name = "Mixed Green Salad with Tomato and Red Onion",
                portion = "100g",
                digestionTime = "1-2 hours",
                healthStatus = HealthStatus.EXCELLENT,
                calories = 50,
                protein = "1g",
                carbs = "5g",
                fat = "3g",
                healthBenefits = listOf(
                    "Rich in vitamins and minerals",
                    "Provides fiber for digestive health",
                    "Contains antioxidants to protect against cell damage"
                ),
                healthConcerns = listOf(
                    "Potential for pesticide residue if not organically grown"
                ),
                analysisSummary = "A nutritious addition to the meal, providing essential vitamins, minerals, and fiber."
            )
        ),
        totalNutrition = TotalNutrition(
            name = "Pork Chop with Potatoes and Salad",
            totalPortion = "450g",
            totalCalories = 560,
            totalProtein = "34g",
            totalCarbs = "42g",
            totalFat = "28g",
            overallHealthStatus = HealthStatus.MODERATE
        ),
        analysisSummary = "The meal provides a good balance of protein and carbohydrates. The high fat content from the pork chop is a concern, suggesting moderation in portion size. The salad adds essential vitamins and minerals. Overall, a moderately healthy meal.",
        dateNTime = "2024-10-05 12:46 PM"
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
        geminiAnalysis = mockData,
        onNavigateBack = {},
        onTryAgain = {}
    )
}

