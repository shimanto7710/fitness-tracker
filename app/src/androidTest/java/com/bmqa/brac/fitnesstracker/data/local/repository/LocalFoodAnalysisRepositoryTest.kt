package com.bmqa.brac.fitnesstracker.data.local.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bmqa.brac.fitnesstracker.data.local.database.FitnessTrackerDatabase
import com.bmqa.brac.fitnesstracker.data.local.database.dao.FoodAnalysisDao
import com.bmqa.brac.fitnesstracker.data.local.database.entities.FoodAnalysisEntity
import com.bmqa.brac.fitnesstracker.data.local.database.entities.FoodItemEntity
import com.bmqa.brac.fitnesstracker.data.local.database.entities.TotalNutritionEntity
import com.bmqa.brac.fitnesstracker.domain.entities.HealthStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalFoodAnalysisRepositoryTest {

    private lateinit var database: FitnessTrackerDatabase
    private lateinit var dao: FoodAnalysisDao
    private lateinit var repository: LocalFoodAnalysisRepository
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(
            context,
            FitnessTrackerDatabase::class.java
        ).allowMainThreadQueries().build()
        
        dao = database.foodAnalysisDao()
        repository = LocalFoodAnalysisRepository(context)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSaveAndRetrieveFoodAnalysis() = runTest {
        // Given
        val testAnalysis = createTestFoodAnalysis()
        
        // When
        val analysisId = repository.saveFoodAnalysisWithId(testAnalysis)
        
        // Then
        assertTrue("Analysis ID should be greater than 0", analysisId > 0)
        
        val retrievedAnalysis = repository.getFoodAnalysisById(analysisId)
        assertNotNull("Retrieved analysis should not be null", retrievedAnalysis)
        assertEquals("Analysis summary should match", testAnalysis.analysisSummary, retrievedAnalysis?.analysisSummary)
        assertEquals("Food items count should match", testAnalysis.foodItems.size, retrievedAnalysis?.foodItems?.size)
        assertNotNull("Total nutrition should not be null", retrievedAnalysis?.totalNutrition)
    }

    @Test
    fun testGetAllFoodAnalyses() = runTest {
        // Given
        val testAnalysis1 = createTestFoodAnalysis("Test Analysis 1")
        val testAnalysis2 = createTestFoodAnalysis("Test Analysis 2")
        
        // When
        repository.saveFoodAnalysisWithId(testAnalysis1)
        repository.saveFoodAnalysisWithId(testAnalysis2)
        
        // Then
        val allAnalyses = repository.getAllFoodAnalyses().first()
        assertEquals("Should have 2 analyses", 2, allAnalyses.size)
    }

    @Test
    fun testGetRecentFoodAnalyses() = runTest {
        // Given
        val testAnalysis1 = createTestFoodAnalysis("Recent Analysis 1")
        val testAnalysis2 = createTestFoodAnalysis("Recent Analysis 2")
        val testAnalysis3 = createTestFoodAnalysis("Recent Analysis 3")
        
        // When
        repository.saveFoodAnalysisWithId(testAnalysis1)
        repository.saveFoodAnalysisWithId(testAnalysis2)
        repository.saveFoodAnalysisWithId(testAnalysis3)
        
        // Then
        val recentAnalyses = repository.getRecentFoodAnalyses(limit = 2).first()
        assertEquals("Should have 2 recent analyses", 2, recentAnalyses.size)
    }

    @Test
    fun testDeleteFoodAnalysis() = runTest {
        // Given
        val testAnalysis = createTestFoodAnalysis()
        val analysisId = repository.saveFoodAnalysisWithId(testAnalysis)
        
        // When
        repository.deleteFoodAnalysis(analysisId)
        
        // Then
        val retrievedAnalysis = repository.getFoodAnalysisById(analysisId)
        assertNull("Analysis should be deleted", retrievedAnalysis)
    }

    @Test
    fun testFoodAnalysisWithImage() = runTest {
        // Given
        val testAnalysis = createTestFoodAnalysis()
        val testBitmap = android.graphics.Bitmap.createBitmap(100, 100, android.graphics.Bitmap.Config.ARGB_8888)
        val imageUri = "content://test/image.jpg"
        
        // When
        val analysisId = repository.saveFoodAnalysisWithId(
            foodAnalysis = testAnalysis,
            imageUri = imageUri,
            imageBitmap = testBitmap
        )
        
        // Then
        val retrievedAnalysis = repository.getFoodAnalysisById(analysisId)
        assertNotNull("Retrieved analysis should not be null", retrievedAnalysis)
        
        // Note: Image path testing would require more complex setup
        // This test verifies the basic save/retrieve functionality with image parameters
    }

    private fun createTestFoodAnalysis(summary: String = "Test analysis summary"): com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis {
        return com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis(
            isError = false,
            errorMessage = "",
            foodItems = listOf(
                com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodItem(
                    name = "Test Food Item",
                    portion = "100g",
                    digestionTime = "2-3 hours",
                    healthStatus = HealthStatus.GOOD,
                    calories = 200,
                    protein = "20g",
                    carbs = "30g",
                    fat = "5g",
                    healthBenefits = listOf("Good source of protein"),
                    healthConcerns = listOf("None"),
                    analysisSummary = "Test food item"
                )
            ),
            totalNutrition = com.bmqa.brac.fitnesstracker.domain.entities.TotalNutrition(
                name = "Test Meal",
                totalPortion = "100g",
                totalCalories = 200,
                totalProtein = "20g",
                totalCarbs = "30g",
                totalFat = "5g",
                overallHealthStatus = HealthStatus.GOOD
            ),
            analysisSummary = summary,
            dateNTime = "2024-01-01 12:00 PM"
        )
    }
}

