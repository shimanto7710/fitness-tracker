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
    data class CaloriesManagement(
        val parent: String? = null
    ) : Route

    @Serializable
    data class GeminiFoodAnalysis(
        val parent: String? = null
    ) : Route

    @Serializable
    data class Nutrition(
        val parent: String? = null,
        val geminiAnalysis: String? = null // JSON string of GeminiFoodAnalysis
    ) : Route

    @Serializable
    object Calendar : Route

    @Serializable
    object DatabaseTest : Route
}

