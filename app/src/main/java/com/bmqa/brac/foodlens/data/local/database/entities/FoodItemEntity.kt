package com.bmqa.brac.foodlens.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bmqa.brac.foodlens.data.local.database.converters.HealthStatusConverter
import com.bmqa.brac.foodlens.data.local.database.converters.StringListConverter
import com.bmqa.brac.foodlens.domain.entities.HealthStatus

@Entity(
    tableName = "food_items",
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
@TypeConverters(HealthStatusConverter::class, StringListConverter::class)
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val analysisId: Long, // Foreign key to FoodAnalysisEntity
    val name: String,
    val portion: String,
    val digestionTime: String,
    val healthStatus: HealthStatus,
    val calories: Int?,
    val protein: String?,
    val carbs: String?,
    val fat: String?,
    val healthBenefits: List<String>,
    val healthConcerns: List<String>,
    val analysisSummary: String
)

