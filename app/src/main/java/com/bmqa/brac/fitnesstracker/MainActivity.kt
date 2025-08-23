package com.bmqa.brac.fitnesstracker

import android.net.Uri
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalUriHandler
import com.bmqa.brac.fitnesstracker.features.imagepicker.ImagePicker
import com.bmqa.brac.fitnesstracker.features.navigation.NavigationScreen
import com.bmqa.brac.fitnesstracker.ui.theme.FitnessTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitnessTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf("navigation") }
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    when (currentScreen) {
        "navigation" -> {
            NavigationScreen(
                onNavigateToImagePicker = {
                    currentScreen = "imagePicker"
                },
                modifier = modifier
            )
            
            // Add test button
            Button(
                onClick = {
                    val intent = android.content.Intent(context, ClarifaiTestActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Test Clarifai API")
            }
        }
        "imagePicker" -> {
            Column(modifier = modifier.fillMaxSize()) {
                // Image Picker Component
                ImagePicker(
                    onImageSelected = { uri ->
                        selectedImageUri = uri
                        Toast.makeText(
                            context,
                            "Image selected: ${uri.lastPathSegment}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                
                // Calories Management Component
                CaloriesManagementScreen(
                    selectedImageUri = selectedImageUri,
                    onBackPressed = {
                        currentScreen = "navigation"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}