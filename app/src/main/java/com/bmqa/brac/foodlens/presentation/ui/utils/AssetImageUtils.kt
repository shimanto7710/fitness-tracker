package com.bmqa.brac.foodlens.presentation.ui.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import java.io.File

/**
 * Utility functions for loading images from assets
 */
object AssetImageUtils {
    
    /**
     * Creates a proper asset URI for loading images from the assets folder
     */
    fun createAssetUri(assetPath: String): String {
        return "file:///android_asset/$assetPath"
    }
    
    /**
     * Composable function to get an asset image painter
     * @param assetPath The path to the asset file (e.g., "food_plate.jpg")
     * @param context The Android context
     * @return A painter that can be used with Image composable
     */
    @Composable
    fun rememberAssetImagePainter(
        assetPath: String,
        context: Context = LocalContext.current
    ) = rememberAsyncImagePainter(
        ImageRequest.Builder(context)
            .data(createAssetUri(assetPath))
            .build()
    )
    
}
