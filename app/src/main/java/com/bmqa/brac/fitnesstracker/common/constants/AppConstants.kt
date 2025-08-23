package com.bmqa.brac.fitnesstracker.common.constants

/**
 * Application-wide constants
 */
object AppConstants {
    
    // File Provider
    const val FILE_PROVIDER_AUTHORITY = ".fileprovider"
    
    // Image Constants
    const val IMAGE_DIRECTORY = "Pictures"
    const val IMAGE_PREFIX = "JPEG_"
    const val IMAGE_EXTENSION = ".jpg"
    
    // Date Format
    const val IMAGE_TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"
    
    // File Size Limits
    const val MAX_IMAGE_SIZE_MB = 10
    const val MAX_IMAGE_SIZE_BYTES = MAX_IMAGE_SIZE_MB * 1024 * 1024
    
    // Supported MIME Types
    val SUPPORTED_IMAGE_TYPES = listOf(
        "image/jpeg",
        "image/jpg",
        "image/png",
        "image/webp",
        "image/gif"
    )
    
    // Navigation Routes (for future navigation implementation)
    object Routes {
        const val HOME = "home"
        const val IMAGE_PICKER = "image_picker"
        const val FITNESS_TRACKER = "fitness_tracker"
    }
    
    // Permissions
    object Permissions {
        const val CAMERA = "camera"
        const val STORAGE = "storage"
    }
}
