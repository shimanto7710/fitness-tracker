package com.rookie.code.presentation.features.nutrition.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.LayoutDirection
import coil.compose.AsyncImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import com.rookie.code.R
import com.rookie.code.common.constants.AppConstants
import com.rookie.code.domain.entities.GeminiFoodAnalysis
import com.rookie.code.domain.entities.GeminiFoodItem
import com.rookie.code.domain.entities.HealthStatus
import com.rookie.code.domain.entities.TotalNutrition

private object NutritionDetailsConstants {
    const val IMAGE_HEIGHT = 300
    const val CARD_ELEVATION = 8
    const val CORNER_RADIUS = 12
    const val BORDER_WIDTH = 1
    const val ICON_SIZE = 24
    const val SMALL_ICON_SIZE = 16
    const val CARD_HEIGHT = 60
    const val PROGRESS_HEIGHT = 8
    const val MAX_DESCRIPTION_LINES = 5
    const val HEALTH_SCORE_EXCELLENT = 0.9f
    const val HEALTH_SCORE_GOOD = 0.7f
    const val HEALTH_SCORE_MODERATE = 0.5f
    const val HEALTH_SCORE_POOR = 0.3f
}


@Composable
fun NutritionDetailsScreen(
    geminiAnalysis: GeminiFoodAnalysis? = null,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val analysis = geminiAnalysis ?: createMockGeminiAnalysis()

    NutritionDetailsContent(
        geminiAnalysis = analysis,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NutritionDetailsContent(
    geminiAnalysis: GeminiFoodAnalysis,
    onNavigateBack: () -> Unit
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
                            .height(NutritionDetailsConstants.IMAGE_HEIGHT.dp)
                    ) {
                        val imageBitmap = geminiAnalysis.base64Image?.let { base64String ->
                            try {
                                val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                            } catch (e: Exception) {
                                null
                            }
                        }
                        
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap.asImageBitmap(),
                                contentDescription = "Food Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Fallback to static image if no dynamic image available
                            AssetOrDrawableImage(
                                assetFileName = "food_plate.jpg",
                                drawableRes = R.drawable.food_plate,
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = "Meal Image"
                            )
                        }
                    }
                }

                // Content card
                item {
                    Card(
                        modifier = Modifier

                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Color.White),
                        elevation = CardDefaults.cardElevation(NutritionDetailsConstants.CARD_ELEVATION.dp),
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
                                    formatTimeForDisplay(geminiAnalysis.dateNTime),
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
                                value = getHealthScoreValue(geminiAnalysis.totalNutrition?.overallHealthStatus),
                                progress = getHealthScoreProgress(geminiAnalysis.totalNutrition?.overallHealthStatus),
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


private fun getHealthScoreValue(healthStatus: HealthStatus?): String {
    return when (healthStatus) {
        HealthStatus.EXCELLENT -> "9/10"
        HealthStatus.GOOD -> "7/10"
        HealthStatus.MODERATE -> "5/10"
        HealthStatus.POOR -> "3/10"
        else -> "N/A"
    }
}


private fun getHealthScoreProgress(healthStatus: HealthStatus?): Float {
    return when (healthStatus) {
        HealthStatus.EXCELLENT -> NutritionDetailsConstants.HEALTH_SCORE_EXCELLENT
        HealthStatus.GOOD -> NutritionDetailsConstants.HEALTH_SCORE_GOOD
        HealthStatus.MODERATE -> NutritionDetailsConstants.HEALTH_SCORE_MODERATE
        HealthStatus.POOR -> NutritionDetailsConstants.HEALTH_SCORE_POOR
        else -> 0.0f
    }
}

@SuppressLint("NewApi")
private fun formatTimeForDisplay(dateNTime: String?): String {
    return try {
        if (!dateNTime.isNullOrEmpty()) {
            // Try to parse the existing dateNTime format
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a")
            val dateTime = LocalDateTime.parse(dateNTime, formatter)
            val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
            dateTime.format(timeFormatter)
        } else {
            // Fallback to current time
            val now = LocalDateTime.now()
            val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
            now.format(timeFormatter)
        }
    } catch (e: Exception) {
        // If parsing fails, use current time
        val now = LocalDateTime.now()
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
        now.format(timeFormatter)
    }
}

@Composable
fun CaloriesCard(
    calories: Int? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(NutritionDetailsConstants.CARD_HEIGHT.dp)
            .clip(RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp))
            .border(
                width = NutritionDetailsConstants.BORDER_WIDTH.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp)
            ),
        shape = RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp),
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
                        .size(NutritionDetailsConstants.ICON_SIZE.dp),
                    painter = painterResource(id = R.drawable.calories),
                    contentDescription = "Calories Icon",
                    tint = Color.Unspecified
                )
            }

            Column(
                modifier = Modifier.padding(start = 24.dp),
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
            .fillMaxWidth()
            .height(NutritionDetailsConstants.CARD_HEIGHT.dp)
            .clip(RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp))
            .border(
                width = NutritionDetailsConstants.BORDER_WIDTH.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp)
            ),
        shape = RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp),
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
                when (label.lowercase()) {
                    "protein" -> Icon(
                        painter = painterResource(id = R.drawable.protein),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(NutritionDetailsConstants.SMALL_ICON_SIZE.dp)
                    )
                    "carb" -> Icon(
                        painter = painterResource(id = R.drawable.carb),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(NutritionDetailsConstants.SMALL_ICON_SIZE.dp)
                    )
                    "fats" -> Icon(
                        painter = painterResource(id = R.drawable.fats),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(NutritionDetailsConstants.SMALL_ICON_SIZE.dp)
                    )
                    "calories" -> Icon(
                        painter = painterResource(id = R.drawable.calories),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(NutritionDetailsConstants.SMALL_ICON_SIZE.dp)
                    )
                    else -> Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(NutritionDetailsConstants.SMALL_ICON_SIZE.dp)
                    )
                }
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
            .height(NutritionDetailsConstants.CARD_HEIGHT.dp)
            .clip(RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp))
            .border(
                width = NutritionDetailsConstants.BORDER_WIDTH.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp)
            ),
        shape = RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp),
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
                    painter = painterResource(id = R.drawable.health),
                    contentDescription = null,
                    tint = Color.Unspecified,
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
                    progress = { progress.coerceIn(0f, 1f) }, // clamp between 0 and 1
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(NutritionDetailsConstants.PROGRESS_HEIGHT.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF4CAF50),
                    trackColor = Color(0xFFE0E0E0)
                )
            }
        }
    }
}

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
                fat = foodItem.fat ?: "N/A",
                portion=foodItem.portion ?:"N/A"
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
    fat: String,
    portion: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp))
            .border(
                width = NutritionDetailsConstants.BORDER_WIDTH.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp)
            ),
        shape = RoundedCornerShape(NutritionDetailsConstants.CORNER_RADIUS.dp),
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
                    painter = painterResource(id = R.drawable.calories),
                    contentDescription = "Calories",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(NutritionDetailsConstants.SMALL_ICON_SIZE.dp)
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = calories + " kcal - "+portion,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.Black,
                maxLines = NutritionDetailsConstants.MAX_DESCRIPTION_LINES,
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
    val mockData = createMockGeminiAnalysis()

    NutritionDetailsContent(
        geminiAnalysis = mockData,
        onNavigateBack = {}
    )
}

