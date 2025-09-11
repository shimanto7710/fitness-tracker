package com.bmqa.brac.foodlens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import com.bmqa.brac.foodlens.presentation.navigation.AppNavigation
import com.bmqa.brac.foodlens.ui.theme.FoodLensTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodLensTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    AppNavigation(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                                bottom = paddingValues.calculateBottomPadding()
                            )
                    )
                }
            }
        }
    }
}