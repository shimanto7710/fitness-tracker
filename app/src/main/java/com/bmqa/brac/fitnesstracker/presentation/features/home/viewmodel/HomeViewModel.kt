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


private object HomeViewModelConstants {
    const val DEFAULT_NUTRITION_VALUE = 0
    const val NUTRITION_UNIT_SUFFIX = "g"
    const val CALORIES_UNIT_SUFFIX = ""
    const val ERROR_MESSAGE_PREFIX = "Error: "
}

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

    fun refreshData() {
        loadSavedAnalyses()
    }

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

    fun getTotalCaloriesForDate(selectedDate: String?): Int {
        if (selectedDate.isNullOrBlank()) return HomeViewModelConstants.DEFAULT_NUTRITION_VALUE
        
        return getFilteredAnalysesForDate(selectedDate)
            .sumOf { analysis ->
                analysis.totalNutrition?.totalCalories ?: HomeViewModelConstants.DEFAULT_NUTRITION_VALUE
            }
    }

    fun getTotalProteinForDate(selectedDate: String?): String {
        if (selectedDate.isNullOrBlank()) return "${HomeViewModelConstants.DEFAULT_NUTRITION_VALUE}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
        
        val totalProtein = getFilteredAnalysesForDate(selectedDate)
            .sumOf { analysis ->
                extractNumericValue(analysis.totalNutrition?.totalProtein) ?: HomeViewModelConstants.DEFAULT_NUTRITION_VALUE.toDouble()
            }
        
        return "${totalProtein.toInt()}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
    }

    fun getTotalCarbsForDate(selectedDate: String?): String {
        if (selectedDate.isNullOrBlank()) return "${HomeViewModelConstants.DEFAULT_NUTRITION_VALUE}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
        
        val totalCarbs = getFilteredAnalysesForDate(selectedDate)
            .sumOf { analysis ->
                extractNumericValue(analysis.totalNutrition?.totalCarbs) ?: HomeViewModelConstants.DEFAULT_NUTRITION_VALUE.toDouble()
            }
        
        return "${totalCarbs.toInt()}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
    }

    fun getTotalFatForDate(selectedDate: String?): String {
        if (selectedDate.isNullOrBlank()) return "${HomeViewModelConstants.DEFAULT_NUTRITION_VALUE}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
        
        val totalFat = getFilteredAnalysesForDate(selectedDate)
            .sumOf { analysis ->
                extractNumericValue(analysis.totalNutrition?.totalFat) ?: HomeViewModelConstants.DEFAULT_NUTRITION_VALUE.toDouble()
            }
        
        return "${totalFat.toInt()}${HomeViewModelConstants.NUTRITION_UNIT_SUFFIX}"
    }

    private fun getFilteredAnalysesForDate(selectedDate: String): List<GeminiFoodAnalysis> {
        return _savedAnalyses.value.filter { analysis ->
            analysis.selectedDate == selectedDate
        }
    }

    /**
     * Extracts numeric value from nutrition strings like "30g", "25.5g", "0g", etc.
     * Returns null if the string is null, empty, or cannot be parsed
     */
    private fun extractNumericValue(nutritionString: String?): Double? {
        if (nutritionString.isNullOrBlank()) return null
        
        // Remove common units and extract the number
        val cleanString = nutritionString
            .replace("g", "", ignoreCase = true)
            .replace("mg", "", ignoreCase = true)
            .replace("kg", "", ignoreCase = true)
            .trim()
        
        return cleanString.toDoubleOrNull()
    }

    fun getAnalysisCountForDate(selectedDate: String?): Int {
        if (selectedDate.isNullOrBlank()) return 0
        return getFilteredAnalysesForDate(selectedDate).size
    }

}
