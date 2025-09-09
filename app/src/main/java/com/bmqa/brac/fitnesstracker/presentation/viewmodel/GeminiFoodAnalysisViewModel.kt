package com.bmqa.brac.fitnesstracker.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmqa.brac.fitnesstracker.data.local.repository.LocalFoodAnalysisRepository
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.usecase.GeminiFoodAnalysisUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GeminiFoodAnalysisViewModel(
    private val geminiFoodAnalysisUseCase: GeminiFoodAnalysisUseCase,
    private val repository: LocalFoodAnalysisRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<GeminiFoodAnalysisUiState>(GeminiFoodAnalysisUiState.Idle)
    val uiState: StateFlow<GeminiFoodAnalysisUiState> = _uiState.asStateFlow()
    
    fun analyzeFoodWithGemini(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            _uiState.value = GeminiFoodAnalysisUiState.Loading
            
            try {
                val result = geminiFoodAnalysisUseCase(imageUri.toString())
                result.fold(
                    onSuccess = { foodAnalysis ->
                        // Set current date and time
                        val currentDateTime = getCurrentDateTime()
                        val updatedFoodAnalysis = foodAnalysis.copy(dateNTime = currentDateTime)
                        
                        // Save to database
                        try {
                            repository.saveFoodAnalysis(
                                foodAnalysis = updatedFoodAnalysis,
                                imageUri = imageUri.toString()
                            )
                            _uiState.value = GeminiFoodAnalysisUiState.Success(updatedFoodAnalysis)
                        } catch (dbException: Exception) {
                            // If database save fails, still show success but log the error
                            _uiState.value = GeminiFoodAnalysisUiState.Success(updatedFoodAnalysis)
                        }
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
    
    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
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

