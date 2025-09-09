package com.bmqa.brac.fitnesstracker.presentation.navigation

import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for the Fitness Tracker app
 */
sealed interface Route {

    @Serializable
    object Home : Route

    @Serializable
    object Dashboard : Route

    @Serializable
    data class CaloriesManagement(
        val parent: String? = null
    ) : Route

@Serializable
data class GeminiFoodAnalysis(
    val parent: String? = null,
    val imageUri: String? = null,
    val selectedDate: String? = null
) : Route

    @Serializable
    data class Nutrition(
        val parent: String? = null,
        val geminiAnalysis: String? = null // JSON string of GeminiFoodAnalysis
    ) : Route
}

