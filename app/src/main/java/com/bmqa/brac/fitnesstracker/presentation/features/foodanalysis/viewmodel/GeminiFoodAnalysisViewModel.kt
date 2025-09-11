package com.bmqa.brac.fitnesstracker.presentation.features.foodanalysis.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.usecase.GeminiFoodAnalysisUseCase
import com.bmqa.brac.fitnesstracker.domain.usecase.SaveFoodAnalysisUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class GeminiFoodAnalysisViewModel(
    private val geminiFoodAnalysisUseCase: GeminiFoodAnalysisUseCase,
    private val saveFoodAnalysisUseCase: SaveFoodAnalysisUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<GeminiFoodAnalysisUiState>(GeminiFoodAnalysisUiState.Idle)
    val uiState: StateFlow<GeminiFoodAnalysisUiState> = _uiState.asStateFlow()
    
    fun analyzeFoodWithGemini(imageUri: Uri, context: Context, selectedDate: String? = null) {
        viewModelScope.launch {
            _uiState.value = GeminiFoodAnalysisUiState.Loading
            
            try {
                android.util.Log.d("GeminiViewModel", "Starting food analysis for URI: $imageUri")
                
                // Load the image bitmap from URI
                val imageBitmap = loadImageFromUri(imageUri, context)
                if (imageBitmap == null) {
                    android.util.Log.e("GeminiViewModel", "Failed to load image from URI: $imageUri")
                    _uiState.value = GeminiFoodAnalysisUiState.Error("Failed to load image. Please try again.")
                    return@launch
                }
                
                android.util.Log.d("GeminiViewModel", "Image loaded successfully, size: ${imageBitmap.width}x${imageBitmap.height}")
                
                val result = geminiFoodAnalysisUseCase(imageUri.toString())
                result.fold(
                    onSuccess = { foodAnalysis ->
                        android.util.Log.d("GeminiViewModel", "Gemini analysis successful")
                        
                        // Set current date and time
                        val currentDateTime = getCurrentDateTime()
                        
                        // Convert image to base64 for navigation
                        val base64Image = try {
                            convertBitmapToBase64(imageBitmap)
                        } catch (e: Exception) {
                            android.util.Log.e("GeminiViewModel", "Failed to convert bitmap to base64", e)
                            ""
                        }
                        
                        val updatedFoodAnalysis = foodAnalysis.copy(
                            dateNTime = currentDateTime,
                            selectedDate = selectedDate,
                            base64Image = base64Image
                        )
                        
                        // Save to database using use case
                        val saveResult = saveFoodAnalysisUseCase(
                            foodAnalysis = updatedFoodAnalysis,
                            imageUri = imageUri.toString(),
                            imageBitmap = imageBitmap
                        )
                        
                        saveResult.fold(
                            onSuccess = {
                                android.util.Log.d("GeminiViewModel", "Food analysis saved to database successfully")
                                _uiState.value = GeminiFoodAnalysisUiState.Success(updatedFoodAnalysis)
                            },
                            onFailure = { dbException ->
                                android.util.Log.e("GeminiViewModel", "Failed to save to database", dbException)
                                // If database save fails, still show success but log the error
                                _uiState.value = GeminiFoodAnalysisUiState.Success(updatedFoodAnalysis)
                            }
                        )
                    },
                    onFailure = { exception ->
                        android.util.Log.e("GeminiViewModel", "Gemini analysis failed", exception)
                        _uiState.value = GeminiFoodAnalysisUiState.Error(exception.message ?: "Unknown error occurred")
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("GeminiViewModel", "Unexpected error during food analysis", e)
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
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            
            // Calculate sample size to prevent OOM
            val sampleSize = calculateInSampleSize(options, 1024, 1024)
            
            // Reset stream and decode with sample size
            inputStream?.close()
            val newInputStream = context.contentResolver.openInputStream(uri)
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
                inJustDecodeBounds = false
            }
            
            BitmapFactory.decodeStream(newInputStream, null, decodeOptions)
        } catch (e: Exception) {
            android.util.Log.e("GeminiViewModel", "Error loading image: ${e.message}", e)
            null
        }
    }
    
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        
        return inSampleSize
    }
    
    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        return try {
            val outputStream = ByteArrayOutputStream()
            // Use lower quality to reduce memory usage
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
            val byteArray = outputStream.toByteArray()
            outputStream.close()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            android.util.Log.e("GeminiViewModel", "Error converting bitmap to base64: ${e.message}", e)
            ""
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

