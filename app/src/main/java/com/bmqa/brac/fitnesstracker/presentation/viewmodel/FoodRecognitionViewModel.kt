package com.bmqa.brac.fitnesstracker.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmqa.brac.fitnesstracker.domain.entities.FoodItem
import com.bmqa.brac.fitnesstracker.domain.usecase.RecognizeFoodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodRecognitionViewModel @Inject constructor(
    private val recognizeFoodUseCase: RecognizeFoodUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FoodRecognitionUiState())
    val uiState: StateFlow<FoodRecognitionUiState> = _uiState.asStateFlow()
    
    fun recognizeFoodFromImage(imageUri: Uri, context: android.content.Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // Convert URI to base64
                val base64Image = uriToBase64(imageUri, context)
                if (base64Image != null) {
                    val result = recognizeFoodUseCase(base64Image)
                    result.fold(
                        onSuccess = { foodItems ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                foodItems = foodItems,
                                error = null
                            )
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = exception.message ?: "Unknown error occurred"
                            )
                        }
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to convert image to base64"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }
    
    private fun uriToBase64(uri: Uri, context: android.content.Context): String? {
        return try {
            val inputStream: java.io.InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                return null
            }
            
            val bytes = inputStream.readBytes()
            android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
        } catch (e: Exception) {
            null
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
