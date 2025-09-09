package com.bmqa.brac.fitnesstracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmqa.brac.fitnesstracker.data.local.repository.LocalFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: LocalFoodAnalysisRepository
) : ViewModel() {
    
    private val _savedAnalyses = MutableStateFlow<List<GeminiFoodAnalysis>>(emptyList())
    val savedAnalyses: StateFlow<List<GeminiFoodAnalysis>> = _savedAnalyses.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private var _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate.asStateFlow()
    
    init {
        loadSavedAnalyses()
    }
    
    fun loadSavedAnalyses() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAllFoodAnalyses().collect { analyses ->
                    _savedAnalyses.value = analyses
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
                // Handle error if needed
            }
        }
    }
    
    fun setSelectedDate(date: String?) {
        _selectedDate.value = date
    }
    
    fun refreshData() {
        loadSavedAnalyses()
    }
}
