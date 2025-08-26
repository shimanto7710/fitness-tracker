package com.bmqa.brac.fitnesstracker.common.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CameraUtils {
    
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir("FoodImages")
        
        // Create directory if it doesn't exist
        storageDir?.mkdirs()
        
        return File.createTempFile(
            "FOOD_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
    
    fun getFileProviderUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
    
    fun getFileProviderAuthority(context: Context): String {
        return "${context.packageName}.fileprovider"
    }
}
