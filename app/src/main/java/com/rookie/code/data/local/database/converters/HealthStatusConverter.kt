package com.rookie.code.data.local.database.converters

import androidx.room.TypeConverter
import com.rookie.code.domain.entities.HealthStatus

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

