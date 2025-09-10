package com.bmqa.brac.fitnesstracker.presentation.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmqa.brac.fitnesstracker.data.local.repository.LocalFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.onCompletion

/**
 * Constants for HomeViewModel operations and calculations.
 */
private object HomeViewModelConstants {
    const val DEFAULT_NUTRITION_VALUE = 0
    const val NUTRITION_UNIT_SUFFIX = "g"
    const val CALORIES_UNIT_SUFFIX = ""
    const val ERROR_MESSAGE_PREFIX = "Error: "
}

/**
 * Sealed class representing the UI state of the Home screen.
 */
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val analyses: List<GeminiFoodAnalysis>,
        val selectedDate: String? = null
    ) : HomeUiState()
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : HomeUiState()
}

/**
 * ViewModel for the Home screen that manages nutrition data and food analyses.
 * 
 * This ViewModel handles:
 * - Loading and managing food analysis data
 * - Calculating nutrition totals for selected dates
 * - Managing UI state and error handling
 * - Providing data for the calendar and nutrition display
 * 
 * @param repository Repository for accessing food analysis data
 */
class HomeViewModel(
    private val repository: LocalFoodAnalysisRepository
) : ViewModel() {
    
    // UI State Management
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    // Individual state flows for backward compatibility
    private val _savedAnalyses = MutableStateFlow<List<GeminiFoodAnalysis>>(emptyList())
    val savedAnalyses: StateFlow<List<GeminiFoodAnalysis>> = _savedAnalyses.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate.asStateFlow()
    
    // Error state
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        loadSavedAnalyses()
    }
    
    /**
     * Loads all saved food analyses from the repository.
     * Updates UI state and handles errors appropriately.
     */
    fun loadSavedAnalyses() {
        viewModelScope.launch {
            repository.getAllFoodAnalyses()
                .onStart {
                    _isLoading.value = true
                    _uiState.value = HomeUiState.Loading
                    _errorMessage.value = null
                }
                .onCompletion {
                    _isLoading.value = false
                }
                .catch { exception ->
                    _isLoading.value = false
                    _errorMessage.value = "${HomeViewModelConstants.ERROR_MESSAGE_PREFIX}${exception.message}"
                    _uiState.value = HomeUiState.Error(
                        message = exception.message ?: "Unknown error occurred",
                        throwable = exception
                    )
                }
                .collect { analyses ->
                    _savedAnalyses.value = analyses
                    _uiState.value = HomeUiState.Success(
                        analyses = analyses,
                        selectedDate = _selectedDate.value
                    )
                }
        }
    }
    
    /**
     * Sets the currently selected date for filtering analyses.
     * 
     * @param date The date string in YYYY-MM-DD format, or null to clear selection
     */
    fun setSelectedDate(date: String?) {
        _selectedDate.value = date
        // Update UI state with new selected date
        when (val currentState = _uiState.value) {
            is HomeUiState.Success -> {
                _uiState.value = currentState.copy(selectedDate = date)
            }
            else -> {
                // If not in success state, just update the selected date
                // The UI state will be updated when data loads
            }
        }
    }
    
    /**
     * Refreshes the data by reloading all food analyses.
     * Useful for pull-to-refresh or manual refresh scenarios.
     */
    fun refreshData() {
        loadSavedAnalyses()
    }
    
    /**
     * Deletes a food analysis from the repository.
     * 
     * @param analysis The analysis to delete
     */
    fun deleteAnalysis(analysis: GeminiFoodAnalysis) {
        viewModelScope.launch {
            try {
                repository.deleteFoodAnalysis(analysis.id)
                // Refresh the list to reflect the deletion
                loadSavedAnalyses()
            } catch (e: Exception) {
                _errorMessage.value = "${HomeViewModelConstants.ERROR_MESSAGE_PREFIX}Failed to delete analysis: ${e.message}"
                _uiState.value = HomeUiState.Error(
                    message = "Failed to delete analysis: ${e.message}",
                    throwable = e
                )
            }
        }
    }
    
    /**
     * Clears any error messages and resets error state.
     */
    fun clearError() {
        _errorMessage.value = null
        // If we're in error state, go back to loading to retry
        if (_uiState.value is HomeUiState.Error) {
            loadSavedAnalyses()
        }
    }
    
    /**
     * Calculates the total calories for the selected date.
     * 
     * @param selectedDate The date to calculate calories for, in YYYY-MM-DD format
     * @return Total calories as integer, or 0 if no date provided or no data found
     */
    fun getTotalCaloriesForDate(selectedDate: String?): Int {
        if (selectedDate.isNullOrBlank()) return HomeViewModelConstants.DEFAULT_NUTRITION_VALUE
        
        return getFilteredAnalysesForDate(selectedDate)
            .sumOf { analysis ->
                analysis.totalNutrition?.totalCalories ?: HomeViewModelConstants.DEFAULT_NUTRITION_VALUE
            }
    }
    
    /**
     * Calculates the total protein for the selected date.
     * 
     * @param selectedDate The date to calculate protein for, in YYYY-MM-DD format
     * @return Total protein as formatted string with unit suffix
     */
    fun getTotalProteinForDate(selectedDate: String?): String {
        if (selectedDate.isNullOrBlank()) return "${HomeViewModelConstants.DEFAULT_NUTRITION_VALUE}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
        
        val totalProtein = getFilteredAnalysesForDate(selectedDate)
            .sumOf { analysis ->
                analysis.totalNutrition?.totalProtein?.toDoubleOrNull() ?: HomeViewModelConstants.DEFAULT_NUTRITION_VALUE.toDouble()
            }
        
        return "${totalProtein.toInt()}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
    }
    
    /**
     * Calculates the total carbohydrates for the selected date.
     * 
     * @param selectedDate The date to calculate carbs for, in YYYY-MM-DD format
     * @return Total carbs as formatted string with unit suffix
     */
    fun getTotalCarbsForDate(selectedDate: String?): String {
        if (selectedDate.isNullOrBlank()) return "${HomeViewModelConstants.DEFAULT_NUTRITION_VALUE}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
        
        val totalCarbs = getFilteredAnalysesForDate(selectedDate)
            .sumOf { analysis ->
                analysis.totalNutrition?.totalCarbs?.toDoubleOrNull() ?: HomeViewModelConstants.DEFAULT_NUTRITION_VALUE.toDouble()
            }
        
        return "${totalCarbs.toInt()}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
    }
    
    /**
     * Calculates the total fat for the selected date.
     * 
     * @param selectedDate The date to calculate fat for, in YYYY-MM-DD format
     * @return Total fat as formatted string with unit suffix
     */
    fun getTotalFatForDate(selectedDate: String?): String {
        if (selectedDate.isNullOrBlank()) return "${HomeViewModelConstants.DEFAULT_NUTRITION_VALUE}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
        
        val totalFat = getFilteredAnalysesForDate(selectedDate)
            .sumOf { analysis ->
                analysis.totalNutrition?.totalFat?.toDoubleOrNull() ?: HomeViewModelConstants.DEFAULT_NUTRITION_VALUE.toDouble()
            }
        
        return "${totalFat.toInt()}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
    }
    
    /**
     * Helper method to get filtered analyses for a specific date.
     * 
     * @param selectedDate The date to filter by, in YYYY-MM-DD format
     * @return List of analyses for the specified date
     */
    private fun getFilteredAnalysesForDate(selectedDate: String): List<GeminiFoodAnalysis> {
        return _savedAnalyses.value.filter { analysis ->
            analysis.selectedDate == selectedDate
        }
    }
    
    /**
     * Gets the count of analyses for the selected date.
     * 
     * @param selectedDate The date to count analyses for
     * @return Number of analyses for the specified date
     */
    fun getAnalysisCountForDate(selectedDate: String?): Int {
        if (selectedDate.isNullOrBlank()) return 0
        return getFilteredAnalysesForDate(selectedDate).size
    }
    
    /**
     * Checks if there are any analyses for the selected date.
     * 
     * @param selectedDate The date to check
     * @return True if there are analyses for the date, false otherwise
     */
    fun hasAnalysesForDate(selectedDate: String?): Boolean {
        return getAnalysisCountForDate(selectedDate) > 0
    }
}
