package com.bmqa.brac.foodlens.data.local.database.converters

import androidx.room.TypeConverter
import com.bmqa.brac.foodlens.domain.entities.HealthStatus

class HealthStatusConverter {
    @TypeConverter
    fun fromHealthStatus(status: HealthStatus): String {
        return status.name
    }

    @TypeConverter
    fun toHealthStatus(status: String): HealthStatus {
        return try {
            HealthStatus.valueOf(status)
        } catch (e: IllegalArgumentException) {
            HealthStatus.UNKNOWN
        }
    }
}

