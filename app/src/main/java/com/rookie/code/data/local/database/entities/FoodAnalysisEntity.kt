package com.rookie.code.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rookie.code.data.local.database.converters.HealthStatusConverter
import com.rookie.code.domain.entities.HealthStatus

@Entity(tableName = "food_analysis")
@TypeConverters(HealthStatusConverter::class)
data class FoodAnalysisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val analysisSummary: String,
    val dateNTime: String? = null,
    val imagePath: String? = null, // Path to the stored image
    val imageUri: String? = null,  // Original URI of the image
    val base64Image: String? = null, // Base64 encoded image
    val selectedDate: String? = null, // Date selected by user in calendar
    val createdAt: Long = System.currentTimeMillis()
)

