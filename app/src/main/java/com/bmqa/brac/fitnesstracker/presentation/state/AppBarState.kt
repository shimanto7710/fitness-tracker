package com.bmqa.brac.fitnesstracker.presentation.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Shared state for managing app bar title and navigation across the app
 */
object AppBarState {
    var title by mutableStateOf("Fitness Tracker")
        private set
    
    var showBackButton by mutableStateOf(false)
        private set
    
    var onBackClick by mutableStateOf<(() -> Unit)?>(null)
        private set
    
    /**
     * Update the app bar title
     */
    fun updateTitle(newTitle: String) {
        title = newTitle
    }
    
    /**
     * Show back button with callback
     */
    fun showBackButton(callback: () -> Unit) {
        showBackButton = true
        onBackClick = callback
    }
    
    /**
     * Hide back button
     */
    fun hideBackButton() {
        showBackButton = false
        onBackClick = null
    }
    
    /**
     * Reset to default state
     */
    fun resetToDefault() {
        title = "Fitness Tracker"
        showBackButton = false
        onBackClick = null
    }
}
