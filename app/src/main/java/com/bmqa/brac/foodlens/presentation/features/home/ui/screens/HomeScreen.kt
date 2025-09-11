package com.bmqa.brac.foodlens.presentation.features.home.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bmqa.brac.foodlens.R
import com.bmqa.brac.foodlens.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.foodlens.presentation.features.home.ui.components.DeleteFoodAnalysisDialog
import com.bmqa.brac.foodlens.presentation.features.home.ui.components.FoodAnalysisCard
import com.bmqa.brac.foodlens.presentation.features.home.ui.components.ImageSelectionDialog
import com.bmqa.brac.foodlens.presentation.features.home.viewmodel.HomeViewModel
import com.bmqa.brac.foodlens.presentation.features.home.viewmodel.HomeUiState
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

import androidx.compose.runtime.*

import androidx.compose.ui.draw.clip

import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmqa.brac.foodlens.domain.entities.GeminiFoodItem
import com.bmqa.brac.foodlens.domain.entities.HealthStatus
import com.bmqa.brac.foodlens.domain.entities.TotalNutrition
import org.koin.androidx.compose.koinViewModel

import java.util.*

private object HomeScreenConstants {
    const val CALENDAR_DAYS_PER_WEEK = 7
    const val CALENDAR_DAY_SIZE = 40
    const val CALENDAR_SPACING = 8
    const val NUTRITION_CARD_SPACING = 16
    const val ANALYSIS_ITEM_HEIGHT = 100
    const val ANALYSIS_IMAGE_SIZE = 80
    const val FAB_SIZE = 56
    const val PREVIEW_YEAR = 2024
    const val PREVIEW_MONTH = 1
    const val PREVIEW_DAY = 15
}

@SuppressLint("NewApi")
@Composable
fun HomeScreen(
    onNavigateToGeminiFoodAnalysis: (String, String) -> Unit = { _, _ -> },
    onNavigateToNutrition: (GeminiFoodAnalysis) -> Unit = {},
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
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    
    // Extract data from UI state
    val savedAnalyses = remember(uiState) {
        val currentState = uiState
        when (currentState) {
            is HomeUiState.Success -> currentState.analyses
            else -> emptyList()
        }
    }
    val isLoading = remember(uiState) {
        val currentState = uiState
        currentState is HomeUiState.Loading
    }
    
    // Handle error state
    val errorMessage = remember(uiState) {
        val currentState = uiState
        when (currentState) {
            is HomeUiState.Error -> currentState.message
            else -> null
        }
    }
    
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
    
    // Calculate nutrition totals dynamically based on saved analyses and selected date
    val totalCalories = remember(savedAnalyses, selectedDate) {
        homeViewModel.getTotalCaloriesForDate(selectedDate.toString())
    }
    val totalProtein = remember(savedAnalyses, selectedDate) {
        homeViewModel.getTotalProteinForDate(selectedDate.toString())
    }
    val totalCarbs = remember(savedAnalyses, selectedDate) {
        homeViewModel.getTotalCarbsForDate(selectedDate.toString())
    }
    val totalFat = remember(savedAnalyses, selectedDate) {
        homeViewModel.getTotalFatForDate(selectedDate.toString())
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

    HomeContent(
        // State variables
        selectedDate = selectedDate,
        currentMonth = currentMonth,
        currentYear = currentYear,
        showMonthDropdown = showMonthDropdown,
        showImagePickerDialog = showImagePickerDialog,
        showDeleteDialog = showDeleteDialog,
        analysisToDelete = analysisToDelete,
        displayedWeekIndex = displayedWeekIndex,
        filteredAnalyses = filteredAnalyses,
        isLoading = isLoading,
        today = today,
        
        // Nutrition data
        totalCalories = totalCalories,
        totalProtein = totalProtein,
        totalCarbs = totalCarbs,
        totalFat = totalFat,
        
        // Error handling
        errorMessage = errorMessage,
        
        // State setters
        onSelectedDateChange = { selectedDate = it },
        onCurrentMonthChange = { currentMonth = it },
        onCurrentYearChange = { currentYear = it },
        onShowMonthDropdownChange = { showMonthDropdown = it },
        onShowImagePickerDialogChange = { showImagePickerDialog = it },
        onShowDeleteDialogChange = { showDeleteDialog = it },
        onAnalysisToDeleteChange = { analysisToDelete = it },
        onDisplayedWeekIndexChange = { displayedWeekIndex = it },
        
        // Business logic callbacks
        onDeleteAnalysis = { analysis -> homeViewModel.deleteAnalysis(analysis) },
        
        // Navigation callbacks
        onNavigateToGeminiFoodAnalysis = onNavigateToGeminiFoodAnalysis,
        onNavigateToNutrition = onNavigateToNutrition,
        modifier = modifier
    )
}

@SuppressLint("NewApi")
@Composable
fun HomeContent(
    // State variables
    selectedDate: LocalDate,
    currentMonth: java.time.Month,
    currentYear: Int,
    showMonthDropdown: Boolean,
    showImagePickerDialog: Boolean,
    showDeleteDialog: Boolean,
    analysisToDelete: GeminiFoodAnalysis?,
    displayedWeekIndex: Int,
    filteredAnalyses: List<GeminiFoodAnalysis>,
    isLoading: Boolean,
    today: LocalDate,
    
    // Nutrition data
    totalCalories: Int?,
    totalProtein: String?,
    totalCarbs: String?,
    totalFat: String?,
    
    // Error handling
    errorMessage: String?,
    
    // State setters
    onSelectedDateChange: (LocalDate) -> Unit,
    onCurrentMonthChange: (java.time.Month) -> Unit,
    onCurrentYearChange: (Int) -> Unit,
    onShowMonthDropdownChange: (Boolean) -> Unit,
    onShowImagePickerDialogChange: (Boolean) -> Unit,
    onShowDeleteDialogChange: (Boolean) -> Unit,
    onAnalysisToDeleteChange: (GeminiFoodAnalysis?) -> Unit,
    onDisplayedWeekIndexChange: (Int) -> Unit,
    
    // Business logic callbacks
    onDeleteAnalysis: (GeminiFoodAnalysis) -> Unit,
    
    // Navigation callbacks
    onNavigateToGeminiFoodAnalysis: (String, String) -> Unit = { _, _ -> },
    onNavigateToNutrition: (GeminiFoodAnalysis) -> Unit = {},
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onShowImagePickerDialogChange(true) },
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
            // Error Message Display
            if (errorMessage != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            
            // Compact Month Header - Integrated with Calendar
            item {
                MonthHeader(
                    currentMonth = currentMonth,
                    currentYear = currentYear,
                    showMonthDropdown = showMonthDropdown,
                    onPreviousMonth = {
                        if (currentMonth == java.time.Month.JANUARY) {
                            onCurrentMonthChange(java.time.Month.DECEMBER)
                            onCurrentYearChange(currentYear - 1)
                        } else {
                            onCurrentMonthChange(currentMonth.minus(1))
                        }
                        onDisplayedWeekIndexChange(0)
                    },
                    onNextMonth = {
                        if (currentMonth == java.time.Month.DECEMBER) {
                            onCurrentMonthChange(java.time.Month.JANUARY)
                            onCurrentYearChange(currentYear + 1)
                        } else {
                            onCurrentMonthChange(currentMonth.plus(1))
                        }
                        onDisplayedWeekIndexChange(0)
                    },
                    onMonthDropdownToggle = { onShowMonthDropdownChange(!showMonthDropdown) },
                    onMonthSelected = { month ->
                        onCurrentMonthChange(month)
                        onDisplayedWeekIndexChange(0)
                        onShowMonthDropdownChange(false)
                    }
                )
            }

            // Calendar Days - Single Row with Week Navigation
            item {
                CalendarWeekView(
                    currentYear = currentYear,
                    currentMonth = currentMonth,
                    selectedDate = selectedDate,
                    displayedWeekIndex = displayedWeekIndex,
                    onDateSelected = { onSelectedDateChange(it) },
                    onPreviousWeek = {
                        if (displayedWeekIndex > 0) {
                            onDisplayedWeekIndexChange(displayedWeekIndex - 1)
                        }
                    },
                    onNextWeek = {
                        val calendarDays = generateCalendarDays(currentYear, currentMonth.value)
                        val weeks = calendarDays.chunked(7)
                        if (displayedWeekIndex < weeks.size - 1) {
                            onDisplayedWeekIndexChange(displayedWeekIndex + 1)
                        }
                    }
                )
            }

            // Nutrition Summary and Food Analyses
            item {
                NutritionAndAnalysesSection(
                    selectedDate = selectedDate,
                    today = today,
                    totalCalories = totalCalories,
                    totalProtein = totalProtein,
                    totalCarbs = totalCarbs,
                    totalFat = totalFat,
                    filteredAnalyses = filteredAnalyses,
                    isLoading = isLoading,
                    onNavigateToNutrition = onNavigateToNutrition,
                    onDeleteAnalysis = { analysis ->
                        onAnalysisToDeleteChange(analysis)
                        onShowDeleteDialogChange(true)
                    }
                )
            }
        }
    }

    // Image Selection Dialog
    if (showImagePickerDialog) {
        ImageSelectionDialog(
            onImageSelected = { imageUri ->
                val selectedDateString = selectedDate.toString()
                onNavigateToGeminiFoodAnalysis(imageUri.toString(), selectedDateString)
            },
            onDismiss = { onShowImagePickerDialogChange(false) }
        )
    }
    
    // Delete Food Analysis Dialog
    DeleteFoodAnalysisDialog(
        isVisible = showDeleteDialog,
        itemName = analysisToDelete?.foodItems?.firstOrNull()?.name ?: "this food analysis",
        onConfirm = {
            analysisToDelete?.let { analysis ->
                onDeleteAnalysis(analysis)
            }
            onShowDeleteDialogChange(false)
            onAnalysisToDeleteChange(null)
        },
        onDismiss = {
            onShowDeleteDialogChange(false)
            onAnalysisToDeleteChange(null)
        }
    )
}

@SuppressLint("NewApi")
@Composable
private fun MonthHeader(
    currentMonth: java.time.Month,
    currentYear: Int,
    showMonthDropdown: Boolean,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onMonthDropdownToggle: () -> Unit,
    onMonthSelected: (java.time.Month) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous Month Button
        IconButton(
            onClick = onPreviousMonth,
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
                modifier = Modifier.clickable { onMonthDropdownToggle() }
            )

            // Month Dropdown Menu
            DropdownMenu(
                expanded = showMonthDropdown,
                onDismissRequest = { onMonthDropdownToggle() }
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
                            onMonthSelected(java.time.Month.of(month))
                        }
                    )
                }
            }
        }

        // Next Month Button
        IconButton(
            onClick = onNextMonth,
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

@SuppressLint("NewApi")
@Composable
private fun CalendarWeekView(
    currentYear: Int,
    currentMonth: java.time.Month,
    selectedDate: LocalDate,
    displayedWeekIndex: Int,
    onDateSelected: (LocalDate) -> Unit,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit
) {
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
            onClick = onPreviousWeek,
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
                    onClick = { onDateSelected(day) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Next Week Button
        IconButton(
            onClick = onNextWeek,
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

@Composable
private fun NutritionAndAnalysesSection(
    selectedDate: LocalDate,
    today: LocalDate,
    totalCalories: Int?,
    totalProtein: String?,
    totalCarbs: String?,
    totalFat: String?,
    filteredAnalyses: List<GeminiFoodAnalysis>,
    isLoading: Boolean,
    onNavigateToNutrition: (GeminiFoodAnalysis) -> Unit,
    onDeleteAnalysis: (GeminiFoodAnalysis) -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))

    // Calories Card
    TotalCalorieCount(
        calories = totalCalories
    )

    Spacer(modifier = Modifier.height(16.dp))

    CalorieTypeDetails(
        protein = totalProtein,
        carbs = totalCarbs,
        fat = totalFat
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
                FoodAnalysisCard(
                    analysis = analysis,
                    onClick = { onNavigateToNutrition(analysis) },
                    onLongPress = { onDeleteAnalysis(analysis) }
                )
            }
        }
    }
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
                    painter = painterResource(id = R.drawable.calories),
                    contentDescription = "Calories Icon",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Composable
fun CalorieTypeDetails(
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
                when (label.lowercase()) {
                    "protein" -> Icon(
                        painter = painterResource(id = R.drawable.protein),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    "carb" -> Icon(
                        painter = painterResource(id = R.drawable.carb),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    "fats" -> Icon(
                        painter = painterResource(id = R.drawable.fats),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    "calories" -> Icon(
                        painter = painterResource(id = R.drawable.calories),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(16.dp)
                    )
                    else -> Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(16.dp)
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
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "HomeContent - With Data")
@Composable
fun HomeContentWithDataPreview() {
    MaterialTheme {
        HomeContent(
            // State variables
            selectedDate = LocalDate.of(HomeScreenConstants.PREVIEW_YEAR, HomeScreenConstants.PREVIEW_MONTH, HomeScreenConstants.PREVIEW_DAY),
            currentMonth = java.time.Month.JANUARY,
            currentYear = HomeScreenConstants.PREVIEW_YEAR,
            showMonthDropdown = false,
            showImagePickerDialog = false,
            showDeleteDialog = false,
            analysisToDelete = null,
            displayedWeekIndex = 0,
            filteredAnalyses = listOf(
                // Mock food analysis data
                GeminiFoodAnalysis(
                    id = 1,
                    isError = false,
                    errorMessage = "",
                    foodItems = listOf(
                        GeminiFoodItem(
                            name = "Grilled Chicken Breast",
                            portion = "150g",
                            digestionTime = "2-3 hours",
                            healthStatus = HealthStatus.GOOD,
                            calories = 165,
                            protein = "31g",
                            carbs = "0g",
                            fat = "3.6g",
                            healthBenefits = listOf("High protein", "Low fat"),
                            healthConcerns = emptyList(),
                            analysisSummary = "Lean protein source"
                        )
                    ),
                    totalNutrition = TotalNutrition(
                        name = "Meal Total",
                        totalPortion = "150g",
                        totalCalories = 165,
                        totalProtein = "31g",
                        totalCarbs = "0g",
                        totalFat = "3.6g",
                        overallHealthStatus = HealthStatus.GOOD
                    ),
                    analysisSummary = "Healthy protein-rich meal",
                    dateNTime = "2024-01-15 12:30:00",
                    imageUri = null,
                    imagePath = null,
                    base64Image = null,
                    selectedDate = "2024-01-15"
                )
            ),
            isLoading = false,
            today = LocalDate.of(HomeScreenConstants.PREVIEW_YEAR, HomeScreenConstants.PREVIEW_MONTH, HomeScreenConstants.PREVIEW_DAY),

            // Nutrition data
            totalCalories = 1650,
            totalProtein = "120g",
            totalCarbs = "180g",
            totalFat = "65g",
            
            // Error handling
            errorMessage = null,

            // State setters
            onSelectedDateChange = {},
            onCurrentMonthChange = {},
            onCurrentYearChange = {},
            onShowMonthDropdownChange = {},
            onShowImagePickerDialogChange = {},
            onShowDeleteDialogChange = {},
            onAnalysisToDeleteChange = {},
            onDisplayedWeekIndexChange = {},

            // Business logic callbacks
            onDeleteAnalysis = {},

            // Navigation callbacks
            onNavigateToGeminiFoodAnalysis = { _, _ -> },
            onNavigateToNutrition = {}
        )
    }
}


/*@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "HomeContent - Default State")
@Composable
fun HomeContentDefaultPreview() {
    MaterialTheme {
        HomeContent(
            // State variables
            selectedDate = LocalDate.of(HomeScreenConstants.PREVIEW_YEAR, HomeScreenConstants.PREVIEW_MONTH, HomeScreenConstants.PREVIEW_DAY),
            currentMonth = java.time.Month.JANUARY,
            currentYear = HomeScreenConstants.PREVIEW_YEAR,
            showMonthDropdown = false,
            showImagePickerDialog = false,
            showDeleteDialog = false,
            analysisToDelete = null,
            displayedWeekIndex = 0,
            filteredAnalyses = emptyList(),
            isLoading = false,
            today = LocalDate.of(HomeScreenConstants.PREVIEW_YEAR, HomeScreenConstants.PREVIEW_MONTH, HomeScreenConstants.PREVIEW_DAY),
            
            // Nutrition data
            totalCalories = 1200,
            totalProtein = "80g",
            totalCarbs = "150g",
            totalFat = "45g",
            
            // State setters
            onSelectedDateChange = {},
            onCurrentMonthChange = {},
            onCurrentYearChange = {},
            onShowMonthDropdownChange = {},
            onShowImagePickerDialogChange = {},
            onShowDeleteDialogChange = {},
            onAnalysisToDeleteChange = {},
            onDisplayedWeekIndexChange = {},
            
            // Business logic callbacks
            onDeleteAnalysis = {},
            
            // Navigation callbacks
            onNavigateToGeminiFoodAnalysis = { _, _ -> },
            onNavigateToNutrition = {}
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "HomeContent - Loading State")
@Composable
fun HomeContentLoadingPreview() {
    MaterialTheme {
        HomeContent(
            // State variables
            selectedDate = LocalDate.of(HomeScreenConstants.PREVIEW_YEAR, HomeScreenConstants.PREVIEW_MONTH, HomeScreenConstants.PREVIEW_DAY),
            currentMonth = java.time.Month.JANUARY,
            currentYear = HomeScreenConstants.PREVIEW_YEAR,
            showMonthDropdown = false,
            showImagePickerDialog = false,
            showDeleteDialog = false,
            analysisToDelete = null,
            displayedWeekIndex = 0,
            filteredAnalyses = emptyList(),
            isLoading = true,
            today = LocalDate.of(HomeScreenConstants.PREVIEW_YEAR, HomeScreenConstants.PREVIEW_MONTH, HomeScreenConstants.PREVIEW_DAY),
            
            // Nutrition data
            totalCalories = null,
            totalProtein = null,
            totalCarbs = null,
            totalFat = null,
            
            // State setters
            onSelectedDateChange = {},
            onCurrentMonthChange = {},
            onCurrentYearChange = {},
            onShowMonthDropdownChange = {},
            onShowImagePickerDialogChange = {},
            onShowDeleteDialogChange = {},
            onAnalysisToDeleteChange = {},
            onDisplayedWeekIndexChange = {},
            
            // Business logic callbacks
            onDeleteAnalysis = {},
            
            // Navigation callbacks
            onNavigateToGeminiFoodAnalysis = { _, _ -> },
            onNavigateToNutrition = {}
        )
    }
}*/

