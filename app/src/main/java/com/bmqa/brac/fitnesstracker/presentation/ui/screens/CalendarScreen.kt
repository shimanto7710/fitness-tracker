package com.bmqa.brac.fitnesstracker.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@SuppressLint("NewApi")
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    var selectedDate by remember { mutableStateOf(today) }
    var currentMonth by remember { mutableStateOf(today.month) }
    var currentYear by remember { mutableStateOf(today.year) }
    var showMonthDropdown by remember { mutableStateOf(false) }
    
    // Initialize displayedWeekIndex to show the week containing today's date
    val initialCalendarDays = generateCalendarDays(today.year, today.month.value)
    val initialWeeks = initialCalendarDays.chunked(7)
    val initialWeekIndex = initialWeeks.indexOfFirst { it.contains(today) }.coerceAtLeast(0)
    var displayedWeekIndex by remember { mutableStateOf(initialWeekIndex) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Month and Year Header with Dropdown
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Month Dropdown
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showMonthDropdown = true }
                ) {
                    Text(
                        text = "${currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault())} $currentYear",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Month",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

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
                                showMonthDropdown = false
                            }
                        )
                    }
                }
            }

            // Navigation Arrows and Today Button
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        if (currentMonth == java.time.Month.JANUARY) {
                            currentMonth = java.time.Month.DECEMBER
                            currentYear--
                        } else {
                            currentMonth = currentMonth.minus(1)
                        }
                        displayedWeekIndex = 0 // Reset to first week of new month
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Previous Month",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                
                // Today Button
                TextButton(
                    onClick = {
                        selectedDate = today
                        currentMonth = today.month
                        currentYear = today.year
                        // Reset to show the week containing today
                        val todayCalendarDays = generateCalendarDays(today.year, today.month.value)
                        val todayWeeks = todayCalendarDays.chunked(7)
                        displayedWeekIndex = todayWeeks.indexOfFirst { it.contains(today) }.coerceAtLeast(0)
                    }
                ) {
                    Text(
                        text = "Today",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                IconButton(
                    onClick = {
                        if (currentMonth == java.time.Month.DECEMBER) {
                            currentMonth = java.time.Month.JANUARY
                            currentYear++
                        } else {
                            currentMonth = currentMonth.plus(1)
                        }
                        displayedWeekIndex = 0 // Reset to first week of new month
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next Month",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        // Calendar Days - Single Row with Week Navigation
        val calendarDays = generateCalendarDays(currentYear, currentMonth.value)
        val weeks = calendarDays.chunked(7)
        val currentWeek = weeks[displayedWeekIndex.coerceIn(0, weeks.size - 1)]

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
                    if (displayedWeekIndex > 0) {
                        displayedWeekIndex--
                    }
                },
                enabled = displayedWeekIndex > 0
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous Week",
                    tint = if (displayedWeekIndex > 0) 
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
                    if (displayedWeekIndex < weeks.size - 1) {
                        displayedWeekIndex++
                    }
                },
                enabled = displayedWeekIndex < weeks.size - 1
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next Week",
                    tint = if (displayedWeekIndex < weeks.size - 1) 
                        MaterialTheme.colorScheme.onBackground 
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Selected Date Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Selected Date",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = selectedDate.format(
                        java.time.format.DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")
                    ),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
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
private fun findCurrentWeek(calendarDays: List<LocalDate>, selectedDate: LocalDate): List<LocalDate> {
    val weeks = calendarDays.chunked(7)
    return weeks.find { week -> week.contains(selectedDate) } ?: weeks.first()
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

@Preview(showBackground = true)
@Composable
private fun CalendarScreenPreview() {
    MaterialTheme {
        CalendarScreen()
    }
}
