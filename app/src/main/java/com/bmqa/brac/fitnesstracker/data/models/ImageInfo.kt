package com.bmqa.brac.fitnesstracker.data.models

import android.net.Uri

/**
 * Data model representing image information
 */
data class ImageInfo(
    val uri: Uri,
    val filePath: String? = null,
    val fileSize: Long? = null,
    val mimeType: String? = null,
    val isValid: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Represents the source of an image
 */
enum class ImageSource {
    CAMERA,
    GALLERY,
    UNKNOWN
}

/**
 * Data model for image picker state
 */
data class ImagePickerState(
    val selectedImage: ImageInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val permissionRequired: String? = null
)
