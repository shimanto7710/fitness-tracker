package com.rookie.code.presentation.navigation

import com.rookie.code.domain.entities.GeminiFoodAnalysis
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for the FoodLens app
 */
sealed interface Route {

    @Serializable
    object Home : Route


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

