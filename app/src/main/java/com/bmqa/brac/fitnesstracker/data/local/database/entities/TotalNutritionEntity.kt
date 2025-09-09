package com.bmqa.brac.fitnesstracker.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bmqa.brac.fitnesstracker.data.local.database.converters.HealthStatusConverter
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus

@Entity(
    tableName = "total_nutrition",
    foreignKeys = [
        ForeignKey(
            entity = FoodAnalysisEntity::class,
            parentColumns = ["id"],
            childColumns = ["analysisId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["analysisId"])]
)
@TypeConverters(HealthStatusConverter::class)
data class TotalNutritionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val analysisId: Long, // Foreign key to FoodAnalysisEntity
    val name: String,
    val totalPortion: String,
    val totalCalories: Int?,
    val totalProtein: String?,
    val totalCarbs: String?,
    val totalFat: String?,
    val overallHealthStatus: HealthStatus
)

