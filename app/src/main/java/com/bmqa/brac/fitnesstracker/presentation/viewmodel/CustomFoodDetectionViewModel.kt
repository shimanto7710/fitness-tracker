package com.bmqa.brac.fitnesstracker.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.domain.usecase.CustomFoodDetectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomFoodDetectionViewModel @Inject constructor(
    private val customFoodDetectionUseCase: CustomFoodDetectionUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<CustomFoodDetectionUiState>(CustomFoodDetectionUiState.Idle)
    val uiState: StateFlow<CustomFoodDetectionUiState> = _uiState.asStateFlow()
    
    fun detectFoodFromImage(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = CustomFoodDetectionUiState.Loading
            
            try {
                val result = customFoodDetectionUseCase(imageUri.toString())
                result.fold(
                    onSuccess = { foodItems ->
                        _uiState.value = CustomFoodDetectionUiState.Success(foodItems)
                    },
                    onFailure = { exception ->
                        _uiState.value = CustomFoodDetectionUiState.Error(exception.message ?: "Unknown error occurred")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = CustomFoodDetectionUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    fun resetState() {
        _uiState.value = CustomFoodDetectionUiState.Idle
    }
}

sealed class CustomFoodDetectionUiState {
    object Idle : CustomFoodDetectionUiState()
    object Loading : CustomFoodDetectionUiState()
    data class Success(val foodItems: List<FoodItem>) : CustomFoodDetectionUiState()
    data class Error(val message: String) : CustomFoodDetectionUiState()
}
