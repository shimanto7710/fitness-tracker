package com.rookie.code.common.utils

import com.rookie.code.domain.entities.GeminiFoodAnalysis
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object JsonUtils {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    fun serializeGeminiFoodAnalysis(analysis: GeminiFoodAnalysis): String {
        return json.encodeToString(analysis)
    }
    
    fun deserializeGeminiFoodAnalysis(jsonString: String): GeminiFoodAnalysis? {
        return try {
            json.decodeFromString<GeminiFoodAnalysis>(jsonString)
        } catch (e: Exception) {
            null
        }
    }
}
