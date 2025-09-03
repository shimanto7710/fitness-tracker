package com.bmqa.brac.fitnesstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bmqa.brac.fitnesstracker.presentation.navigation.FitnessTrackerNavGraph
import com.bmqa.brac.fitnesstracker.presentation.ui.components.AppBar
import com.bmqa.brac.fitnesstracker.presentation.state.AppBarState
import com.bmqa.brac.fitnesstracker.ui.theme.FitnessTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitnessTrackerTheme {
                // Suppress lint warning for unused padding parameter
                @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title = AppBarState.title,
                            showBackButton = AppBarState.showBackButton,
                            onBackClick = AppBarState.onBackClick
                        )
                    }
                ) { paddingValues ->
                    FitnessTrackerNavGraph(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }
        }
    }
}