package com.bmqa.brac.fitnesstracker.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse
import com.bmqa.brac.fitnesstracker.domain.usecase.AnalyzeFoodImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OpenRouterFoodAnalysisViewModel @Inject constructor(
    private val analyzeFoodImageUseCase: AnalyzeFoodImageUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<OpenRouterFoodAnalysisUiState>(OpenRouterFoodAnalysisUiState.Idle)
    val uiState: StateFlow<OpenRouterFoodAnalysisUiState> = _uiState.asStateFlow()
    
    fun analyzeFoodImage(imageUri: Uri, customText: String? = null) {
        viewModelScope.launch {
            _uiState.value = OpenRouterFoodAnalysisUiState.Loading
            
            try {
                val result = analyzeFoodImageUseCase(imageUri, customText)
                result.fold(
                    onSuccess = { response ->
                        _uiState.value = OpenRouterFoodAnalysisUiState.Success(response)
                    },
                    onFailure = { exception ->
                        _uiState.value = OpenRouterFoodAnalysisUiState.Error(
                            exception.message ?: "Unknown error occurred"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = OpenRouterFoodAnalysisUiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }
    
    fun resetState() {
        _uiState.value = OpenRouterFoodAnalysisUiState.Idle
    }
    
    fun clearError() {
        if (_uiState.value is OpenRouterFoodAnalysisUiState.Error) {
            _uiState.value = OpenRouterFoodAnalysisUiState.Idle
        }
    }
}

sealed class OpenRouterFoodAnalysisUiState {
    object Idle : OpenRouterFoodAnalysisUiState()
    object Loading : OpenRouterFoodAnalysisUiState()
    data class Success(val response: OpenRouterResponse) : OpenRouterFoodAnalysisUiState()
    data class Error(val message: String) : OpenRouterFoodAnalysisUiState()
}
