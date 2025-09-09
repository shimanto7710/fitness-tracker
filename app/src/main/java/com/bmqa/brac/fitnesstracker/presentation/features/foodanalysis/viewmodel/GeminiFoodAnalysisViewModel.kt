package com.bmqa.brac.fitnesstracker.presentation.features.foodanalysis.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class GeminiFoodAnalysisViewModel(
    private val geminiFoodAnalysisUseCase: GeminiFoodAnalysisUseCase,
    private val repository: LocalFoodAnalysisRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<GeminiFoodAnalysisUiState>(GeminiFoodAnalysisUiState.Idle)
    val uiState: StateFlow<GeminiFoodAnalysisUiState> = _uiState.asStateFlow()
    
    fun analyzeFoodWithGemini(imageUri: Uri, context: Context, selectedDate: String? = null) {
        viewModelScope.launch {
            _uiState.value = GeminiFoodAnalysisUiState.Loading
            
            try {
                // Load the image bitmap from URI
                val imageBitmap = loadImageFromUri(imageUri, context)
                
                val result = geminiFoodAnalysisUseCase(imageUri.toString())
                result.fold(
                    onSuccess = { foodAnalysis ->
                        // Set current date and time
                        val currentDateTime = getCurrentDateTime()
                        val updatedFoodAnalysis = foodAnalysis.copy(
                            dateNTime = currentDateTime,
                            selectedDate = selectedDate
                        )
                        
                        // Save to database with image bitmap
                        try {
                            repository.saveFoodAnalysis(
                                foodAnalysis = updatedFoodAnalysis,
                                imageUri = imageUri.toString(),
                                imageBitmap = imageBitmap
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
    
    private fun loadImageFromUri(uri: Uri, context: Context): Bitmap? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
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

