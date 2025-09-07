package com.bmqa.brac.fitnesstracker.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.usecase.GeminiFoodAnalysisUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeminiFoodAnalysisViewModel(
    private val geminiFoodAnalysisUseCase: GeminiFoodAnalysisUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<GeminiFoodAnalysisUiState>(GeminiFoodAnalysisUiState.Idle)
    val uiState: StateFlow<GeminiFoodAnalysisUiState> = _uiState.asStateFlow()
    
    fun analyzeFoodWithGemini(imageUri: Uri) {
        viewModelScope.launch {
            _uiState.value = GeminiFoodAnalysisUiState.Loading
            
            try {
                val result = geminiFoodAnalysisUseCase(imageUri.toString())
                result.fold(
                    onSuccess = { foodAnalysis ->
                        _uiState.value = GeminiFoodAnalysisUiState.Success(foodAnalysis)
                    },
                    onFailure = { exception ->
                        _uiState.value = GeminiFoodAnalysisUiState.Error(exception.message ?: "Unknown error occurred")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = GeminiFoodAnalysisUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    fun resetState() {
        _uiState.value = GeminiFoodAnalysisUiState.Idle
    }
}

sealed class GeminiFoodAnalysisUiState {
    object Idle : GeminiFoodAnalysisUiState()
    object Loading : GeminiFoodAnalysisUiState()
    data class Success(val foodAnalysis: GeminiFoodAnalysis) : GeminiFoodAnalysisUiState()
    data class Error(val message: String) : GeminiFoodAnalysisUiState()
}

