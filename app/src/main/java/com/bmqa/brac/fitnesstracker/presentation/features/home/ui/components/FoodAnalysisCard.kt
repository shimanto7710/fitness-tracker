package com.bmqa.brac.fitnesstracker.presentation.features.home.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import android.graphics.BitmapFactory
import android.util.Base64
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FoodAnalysisCard(
    analysis: GeminiFoodAnalysis,
    onClick: () -> Unit,
    onLongPress: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongPress() }
                )
            }
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image placeholder or actual image
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                val imageBitmap = analysis.base64Image?.let { base64String ->
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
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Food Image",
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Food name or analysis summary
                Text(
                    text = analysis.foodItems.firstOrNull()?.name ?: "Food Analysis",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Date and time
                analysis.dateNTime?.let { dateTime ->
                    Text(
                        text = formatDateTime(dateTime),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Health status and calories
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HealthStatusChip(
                        healthStatus = analysis.totalNutrition?.overallHealthStatus ?: HealthStatus.UNKNOWN,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    
                    analysis.totalNutrition?.totalCalories?.let { calories ->
                        Text(
                            text = "$calories kcal",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            // Arrow indicator
            Icon(
                imageVector = Icons.Default.AccountCircle, // You can replace with arrow icon
                contentDescription = "View Details",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun HealthStatusChip(
    healthStatus: HealthStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (healthStatus) {
        HealthStatus.EXCELLENT -> Color(0xFF4CAF50) to Color.White
        HealthStatus.GOOD -> Color(0xFF8BC34A) to Color.White
        HealthStatus.MODERATE -> Color(0xFFFF9800) to Color.White
        HealthStatus.POOR -> Color(0xFFF44336) to Color.White
        HealthStatus.UNKNOWN -> Color(0xFF9E9E9E) to Color.White
    }
    
    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = healthStatus.name,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatDateTime(dateTimeString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTimeString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateTimeString // Return original string if parsing fails
    }
}
