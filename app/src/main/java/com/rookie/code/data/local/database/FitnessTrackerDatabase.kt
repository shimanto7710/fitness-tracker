package com.rookie.code.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.rookie.code.data.local.database.converters.HealthStatusConverter
import com.rookie.code.data.local.database.converters.StringListConverter
import com.rookie.code.data.local.database.dao.FoodAnalysisDao
import com.rookie.code.data.local.database.entities.FoodAnalysisEntity
import com.rookie.code.data.local.database.entities.FoodItemEntity
import com.rookie.code.data.local.database.entities.TotalNutritionEntity

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
abstract class FoodLensDatabase : RoomDatabase() {

    abstract fun foodAnalysisDao(): FoodAnalysisDao

    companion object {
        @Volatile
        private var INSTANCE: FoodLensDatabase? = null

        fun getDatabase(context: Context): FoodLensDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodLensDatabase::class.java,
                    "foodlens_database"
                )
                .fallbackToDestructiveMigration() // For development - remove in production
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

