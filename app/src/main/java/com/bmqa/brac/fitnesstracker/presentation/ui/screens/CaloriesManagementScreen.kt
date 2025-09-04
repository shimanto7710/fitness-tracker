package com.bmqa.brac.fitnesstracker.presentation.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.presentation.state.AppBarState
import com.bmqa.brac.fitnesstracker.presentation.ui.components.ImagePicker
import com.bmqa.brac.fitnesstracker.presentation.ui.components.ClarifaiFoodRecognizer
import com.bmqa.brac.fitnesstracker.ui.theme.Dimensions

@Composable
fun CaloriesManagementScreen(
    onNavigateBack: () -> Unit
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    // Update app bar title and show back button
    LaunchedEffect(Unit) {
        AppBarState.updateTitle(AppConstants.UiText.CALORIES_MANAGEMENT_TITLE)
        AppBarState.showBackButton(onNavigateBack)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.spacingMedium)
    ) {
        
        // Image picker component with bottom sheet dialog
        ImagePicker(
            onImageSelected = { uri ->
                selectedImageUri = uri
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
        
        // Food recognizer component
        ClarifaiFoodRecognizer(
            selectedImageUri = selectedImageUri
        )
    }
}