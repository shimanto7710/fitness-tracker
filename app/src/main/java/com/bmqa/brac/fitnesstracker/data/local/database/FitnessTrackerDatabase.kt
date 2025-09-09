package com.bmqa.brac.fitnesstracker.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.bmqa.brac.fitnesstracker.data.local.database.converters.HealthStatusConverter
import com.bmqa.brac.fitnesstracker.data.local.database.converters.StringListConverter
import com.bmqa.brac.fitnesstracker.data.local.database.dao.FoodAnalysisDao
import com.bmqa.brac.fitnesstracker.data.local.database.entities.FoodAnalysisEntity
import com.bmqa.brac.fitnesstracker.data.local.database.entities.FoodItemEntity
import com.bmqa.brac.fitnesstracker.data.local.database.entities.TotalNutritionEntity

@Database(
    entities = [
        FoodAnalysisEntity::class,
        FoodItemEntity::class,
        TotalNutritionEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(
    HealthStatusConverter::class,
    StringListConverter::class
)
abstract class FitnessTrackerDatabase : RoomDatabase() {

    abstract fun foodAnalysisDao(): FoodAnalysisDao

    companion object {
        @Volatile
        private var INSTANCE: FitnessTrackerDatabase? = null

        fun getDatabase(context: Context): FitnessTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitnessTrackerDatabase::class.java,
                    "fitness_tracker_database"
                )
                .fallbackToDestructiveMigration() // For development - remove in production
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

