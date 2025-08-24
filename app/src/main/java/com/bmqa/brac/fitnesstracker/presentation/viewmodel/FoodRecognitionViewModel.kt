package com.bmqa.brac.fitnesstracker.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.presentation.service.ClarifaiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodRecognitionViewModel @Inject constructor(
    private val clarifaiService: ClarifaiService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FoodRecognitionUiState())
    val uiState: StateFlow<FoodRecognitionUiState> = _uiState.asStateFlow()
    
    fun recognizeFoodFromImage(imageUri: Uri, context: android.content.Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val foodItems = clarifaiService.recognizeFoodFromImage(imageUri, context)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    foodItems = foodItems,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
    
    fun selectFood(food: FoodItem) {
        _uiState.value = _uiState.value.copy(selectedFood = food)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class FoodRecognitionUiState(
    val isLoading: Boolean = false,
    val foodItems: List<FoodItem> = emptyList(),
    val selectedFood: FoodItem? = null,
    val error: String? = null
)
