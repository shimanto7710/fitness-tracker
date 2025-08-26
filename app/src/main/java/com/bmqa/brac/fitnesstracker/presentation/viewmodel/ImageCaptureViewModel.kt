package com.bmqa.brac.fitnesstracker.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageCaptureViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow<ImageCaptureUiState>(ImageCaptureUiState.Idle)
    val uiState: StateFlow<ImageCaptureUiState> = _uiState.asStateFlow()
    
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()
    
    fun selectImageFromGallery(uri: Uri) {
        viewModelScope.launch {
            _selectedImageUri.value = uri
            _uiState.value = ImageCaptureUiState.ImageSelected(uri)
        }
    }
    
    fun captureImageFromCamera(uri: Uri) {
        viewModelScope.launch {
            _selectedImageUri.value = uri
            _uiState.value = ImageCaptureUiState.ImageSelected(uri)
        }
    }
    
    fun resetImage() {
        viewModelScope.launch {
            _selectedImageUri.value = null
            _uiState.value = ImageCaptureUiState.Idle
        }
    }
    
    fun confirmImageSelection() {
        viewModelScope.launch {
            _selectedImageUri.value?.let { uri ->
                _uiState.value = ImageCaptureUiState.ImageConfirmed(uri)
            }
        }
    }
    
    fun getSelectedImageUri(): Uri? {
        return _selectedImageUri.value
    }
}

sealed class ImageCaptureUiState {
    object Idle : ImageCaptureUiState()
    data class ImageSelected(val uri: Uri) : ImageCaptureUiState()
    data class ImageConfirmed(val uri: Uri) : ImageCaptureUiState()
    data class Error(val message: String) : ImageCaptureUiState()
}
