package com.bmqa.brac.fitnesstracker.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Reusable InfoCard component for displaying key-value information
 */
@Composable
fun InfoCard(
    title: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF5F5F5),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

/**
 * Reusable component for displaying label-value pairs
 */
@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Color.DarkGray,
            modifier = Modifier.width(80.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
    }
}
