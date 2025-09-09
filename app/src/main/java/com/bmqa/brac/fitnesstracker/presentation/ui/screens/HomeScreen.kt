package com.bmqa.brac.fitnesstracker.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import com.bmqa.brac.fitnesstracker.presentation.ui.components.ImagePickerDialog
import com.bmqa.brac.fitnesstracker.presentation.ui.components.FoodAnalysisListItem
import com.bmqa.brac.fitnesstracker.presentation.ui.components.DeleteConfirmationDialog
import com.bmqa.brac.fitnesstracker.presentation.viewmodel.HomeViewModel
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@SuppressLint("NewApi")
@Composable
fun HomeScreen(
    onNavigateToCaloriesManagement: () -> Unit = {},
    onNavigateToGeminiFoodAnalysis: (String, String) -> Unit = { _, _ -> },
    onNavigateToNutrition: (GeminiFoodAnalysis) -> Unit = {},
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToDatabaseTest: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    var selectedDate by remember { mutableStateOf(today) }
    var currentMonth by remember { mutableStateOf(today.month) }
    var currentYear by remember { mutableStateOf(today.year) }
    var showMonthDropdown by remember { mutableStateOf(false) }
    var showImagePickerDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var analysisToDelete by remember { mutableStateOf<GeminiFoodAnalysis?>(null) }
    val homeViewModel: HomeViewModel = koinViewModel()
    val savedAnalyses by homeViewModel.savedAnalyses.collectAsStateWithLifecycle()
    val isLoading by homeViewModel.isLoading.collectAsStateWithLifecycle()
    
    // Filter analyses by selected date
    val filteredAnalyses = remember(savedAnalyses, selectedDate) {
        if (selectedDate != null) {
            val selectedDateString = selectedDate.toString()
            savedAnalyses.filter { analysis ->
                analysis.selectedDate == selectedDateString
            }
        } else {
            savedAnalyses
        }
    }
    
    // Update selected date in viewmodel when it changes
    LaunchedEffect(selectedDate) {
        homeViewModel.setSelectedDate(selectedDate.toString())
    }

    // Initialize displayedWeekIndex to show the week containing today's date
    val initialCalendarDays = generateCalendarDays(today.year, today.month.value)
    val initialWeeks = initialCalendarDays.chunked(7)
    val initialWeekIndex = initialWeeks.indexOfFirst { it.contains(today) }.coerceAtLeast(0)
    var displayedWeekIndex by remember { mutableStateOf(initialWeekIndex) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showImagePickerDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Food Analysis"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
        // Compact Month Header - Integrated with Calendar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous Month Button
                IconButton(
                    onClick = {
                        if (currentMonth == java.time.Month.JANUARY) {
                            currentMonth = java.time.Month.DECEMBER
                            currentYear--
                        } else {
                            currentMonth = currentMonth.minus(1)
                        }
                        displayedWeekIndex = 0
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Month",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Month/Year Display - Clickable for dropdown
                Box {
                    Text(
                        text = "${currentMonth.getDisplayName(TextStyle.SHORT, Locale.getDefault())} $currentYear",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.clickable { showMonthDropdown = true }
                    )

                    // Month Dropdown Menu
                    DropdownMenu(
                        expanded = showMonthDropdown,
                        onDismissRequest = { showMonthDropdown = false }
                    ) {
                        (1..12).forEach { month ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = java.time.Month.of(month)
                                            .getDisplayName(TextStyle.FULL, Locale.getDefault())
                                    )
                                },
                                onClick = {
                                    currentMonth = java.time.Month.of(month)
                                    displayedWeekIndex = 0 // Reset to first week of new month
                                    showMonthDropdown = false
                                }
                            )
                        }
                    }
                }

                // Next Month Button
                IconButton(
                    onClick = {
                        if (currentMonth == java.time.Month.DECEMBER) {
                            currentMonth = java.time.Month.JANUARY
                            currentYear++
                        } else {
                            currentMonth = currentMonth.plus(1)
                        }
                        displayedWeekIndex = 0
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Month",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Calendar Days - Single Row with Week Navigation
        item {
            val calendarDays = generateCalendarDays(currentYear, currentMonth.value)
            val weeks = calendarDays.chunked(7)
            val safeWeekIndex = displayedWeekIndex.coerceIn(0, weeks.size - 1)
            val currentWeek = weeks[safeWeekIndex]

            // Days of Week Header - Aligned with calendar days
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Spacer to align with previous week button
                Spacer(modifier = Modifier.width(48.dp))

                // Day names aligned with calendar days
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    currentWeek.forEach { day ->
                        Text(
                            text = day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Spacer to align with next week button
                Spacer(modifier = Modifier.width(48.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous Week Button
                IconButton(
                    onClick = {
                        if (safeWeekIndex > 0) {
                            displayedWeekIndex--
                        }
                    },
                    enabled = safeWeekIndex > 0
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Week",
                        tint = if (safeWeekIndex > 0)
                            MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                }

                // Calendar Days
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    currentWeek.forEach { day ->
                        CalendarDayItem(
                            day = day,
                            isSelected = day == selectedDate,
                            isCurrentMonth = day.month == currentMonth,
                            onClick = { selectedDate = day },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Next Week Button
                IconButton(
                    onClick = {
                        if (safeWeekIndex < weeks.size - 1) {
                            displayedWeekIndex++
                        }
                    },
                    enabled = safeWeekIndex < weeks.size - 1
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Next Week",
                        tint = if (safeWeekIndex < weeks.size - 1)
                            MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Calories Card
            TotalCalorieCount(
                calories = 250 // You can make this dynamic based on selected date
            )

            Spacer(modifier = Modifier.height(16.dp))

             CalorieTypeDetails(
                 protein = "30g",
                 carbs = "50g",
                 fat = "10g"
             )

            Spacer(modifier = Modifier.height(16.dp))

            // Saved Food Analyses List
            Text(
                text = "Recent Food Analyses",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (filteredAnalyses.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (selectedDate == today) "No food analyses for today" else "No food analyses for selected date",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap the + button to analyze a meal for this date!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filteredAnalyses.forEach { analysis ->
                        FoodAnalysisListItem(
                            analysis = analysis,
                            onClick = {
                                onNavigateToNutrition(analysis)
                            },
                            onLongPress = {
                                analysisToDelete = analysis
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
        }
    }
    
    // Image Picker Dialog
    if (showImagePickerDialog) {
        ImagePickerDialog(
            onImageSelected = { imageUri ->
                // Navigate to Gemini Food Analysis with the selected image and date
                val selectedDateString = selectedDate.toString()
                onNavigateToGeminiFoodAnalysis(imageUri.toString(), selectedDateString)
            },
            onDismiss = { showImagePickerDialog = false }
        )
    }
    
    // Delete Confirmation Dialog
    DeleteConfirmationDialog(
        isVisible = showDeleteDialog,
        itemName = analysisToDelete?.foodItems?.firstOrNull()?.name ?: "this food analysis",
        onConfirm = {
            analysisToDelete?.let { analysis ->
                homeViewModel.deleteAnalysis(analysis)
            }
            showDeleteDialog = false
            analysisToDelete = null
        },
        onDismiss = {
            showDeleteDialog = false
            analysisToDelete = null
        }
    )
}

@SuppressLint("NewApi")
@Composable
private fun CalendarDayItem(
    day: LocalDate,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isToday = day == LocalDate.now()

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    isToday -> MaterialTheme.colorScheme.secondary
                    isCurrentMonth -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    else -> Color.Transparent
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.dayOfMonth.toString(),
            fontSize = 18.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
            color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                isToday -> MaterialTheme.colorScheme.onSecondary
                isCurrentMonth -> MaterialTheme.colorScheme.onBackground
                else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            },
            textAlign = TextAlign.Center
        )
    }
}

@SuppressLint("NewApi")
private fun generateCalendarDays(year: Int, month: Int): List<LocalDate> {
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth())

    // Get the first Monday of the week containing the first day of the month
    val firstMonday = firstDayOfMonth.minusDays((firstDayOfMonth.dayOfWeek.value - 1).toLong())

    // Get the last Sunday of the week containing the last day of the month
    val lastSunday = lastDayOfMonth.plusDays((7 - lastDayOfMonth.dayOfWeek.value).toLong())

    val days = mutableListOf<LocalDate>()
    var currentDay = firstMonday

    while (!currentDay.isAfter(lastSunday)) {
        days.add(currentDay)
        currentDay = currentDay.plusDays(1)
    }

    return days
}

@Composable
fun TotalCalorieCount(
    calories: Int? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
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
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 48.dp, end = 48.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    calories?.toString() ?: "N/A",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("Calories", fontSize = 10.sp)

            }

            // Icon box
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp),
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Calories Icon",
                    tint = Color.Black
                )
            }

        }
    }
}

@Composable
private fun CalorieTypeDetails(
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
        CalorieTypeCard(label = "Protein", value = protein ?: "N/A", modifier = Modifier.weight(1f))
        CalorieTypeCard(label = "Carb", value = carbs ?: "N/A", modifier = Modifier.weight(1f))
        CalorieTypeCard(label = "Fats", value = fat ?: "N/A", modifier = Modifier.weight(1f))
    }
}

@Composable
fun CalorieTypeCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth() // slightly taller for balance
            .height(80.dp)
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
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}

@Composable
private fun RecentlyUsedList() {
    val recentlyUsedItems = listOf(
        RecentlyUsedItem(
            id = 1,
            name = "Grilled Chicken Breast",
            totalCalories = "165 kcal",
            imageUrl = "https://example.com/chicken.jpg"
        ),
        RecentlyUsedItem(
            id = 2,
            name = "Brown Rice Bowl",
            totalCalories = "220 kcal",
            imageUrl = "https://example.com/rice.jpg"
        ),
        RecentlyUsedItem(
            id = 3,
            name = "Mixed Green Salad",
            totalCalories = "85 kcal",
            imageUrl = "https://example.com/salad.jpg"
        ),
        RecentlyUsedItem(
            id = 4,
            name = "Greek Yogurt",
            totalCalories = "100 kcal",
            imageUrl = "https://example.com/yogurt.jpg"
        )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        recentlyUsedItems.forEach { item ->
            RecentlyUsedItemCard(item = item)
        }
    }
}

@Composable
private fun RecentlyUsedItemCard(
    item: RecentlyUsedItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
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
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side image with rounded corners on left side only
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            bottomStart = 12.dp,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .background(Color(0xFFF0F0F0))
            ) {
                // Placeholder for image - you can replace with actual image loading
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = item.name,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }

            // Content area
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.totalCalories,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }
    }
}

data class RecentlyUsedItem(
    val id: Int,
    val name: String,
    val totalCalories: String,
    val imageUrl: String
)

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}