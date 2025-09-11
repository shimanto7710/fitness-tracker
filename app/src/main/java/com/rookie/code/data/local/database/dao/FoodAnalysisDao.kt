package com.rookie.code.data.local.database.dao

import androidx.room.*
import com.rookie.code.data.local.database.entities.FoodAnalysisEntity
import com.rookie.code.data.local.database.entities.FoodItemEntity
import com.rookie.code.data.local.database.entities.TotalNutritionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodAnalysisDao {

    // Food Analysis operations
    @Query("SELECT * FROM food_analysis ORDER BY createdAt DESC")
    fun getAllFoodAnalyses(): Flow<List<FoodAnalysisEntity>>

    @Query("SELECT * FROM food_analysis WHERE id = :id")
    suspend fun getFoodAnalysisById(id: Long): FoodAnalysisEntity?

    @Query("SELECT * FROM food_analysis ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentFoodAnalyses(limit: Int = 10): Flow<List<FoodAnalysisEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodAnalysis(foodAnalysis: FoodAnalysisEntity): Long

    @Update
    suspend fun updateFoodAnalysis(foodAnalysis: FoodAnalysisEntity)

    @Delete
    suspend fun deleteFoodAnalysis(foodAnalysis: FoodAnalysisEntity)

    @Query("DELETE FROM food_analysis WHERE id = :id")
    suspend fun deleteFoodAnalysisById(id: Long)

    // Food Items operations
    @Query("SELECT * FROM food_items WHERE analysisId = :analysisId")
    suspend fun getFoodItemsByAnalysisId(analysisId: Long): List<FoodItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItems(foodItems: List<FoodItemEntity>)

    @Delete
    suspend fun deleteFoodItems(foodItems: List<FoodItemEntity>)

    // Total Nutrition operations
    @Query("SELECT * FROM total_nutrition WHERE analysisId = :analysisId")
    suspend fun getTotalNutritionByAnalysisId(analysisId: Long): TotalNutritionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTotalNutrition(totalNutrition: TotalNutritionEntity)

    @Delete
    suspend fun deleteTotalNutrition(totalNutrition: TotalNutritionEntity)

    // Complex queries for complete data
    @Transaction
    @Query("SELECT * FROM food_analysis WHERE id = :id")
    suspend fun getCompleteFoodAnalysisById(id: Long): CompleteFoodAnalysis?

    @Transaction
    @Query("SELECT * FROM food_analysis ORDER BY createdAt DESC")
    fun getAllCompleteFoodAnalyses(): Flow<List<CompleteFoodAnalysis>>

    @Transaction
    @Query("SELECT * FROM food_analysis ORDER BY createdAt DESC LIMIT :limit")
    fun getRecentCompleteFoodAnalyses(limit: Int = 10): Flow<List<CompleteFoodAnalysis>>
}

// Data class for complete food analysis with relationships
data class CompleteFoodAnalysis(
    @Embedded val foodAnalysis: FoodAnalysisEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "analysisId"
    )
    val foodItems: List<FoodItemEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "analysisId"
    )
    val totalNutrition: TotalNutritionEntity?
)

