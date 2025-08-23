package com.bmqa.brac.fitnesstracker.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object ImageUtils {
    
    /**
     * Get the file path from a content URI
     */
    fun getFilePathFromUri(context: Context, uri: Uri): String? {
        return try {
            when (uri.scheme) {
                "content" -> {
                    val cursor = context.contentResolver.query(uri, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val columnIndex = it.getColumnIndex(android.provider.MediaStore.Images.Media.DATA)
                            if (columnIndex != -1) {
                                it.getString(columnIndex)
                            } else null
                        } else null
                    }
                }
                "file" -> uri.path
                else -> null
            }
        } catch (e: Exception) {
            Log.e("ImageUtils", "Error getting file path from URI", e)
            null
        }
    }
    
    /**
     * Copy content URI to app's private directory and return the new URI
     */
    fun copyUriToAppDirectory(context: Context, uri: Uri, fileName: String): Uri? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputFile = File(context.filesDir, fileName)
            
            inputStream?.use { input ->
                FileOutputStream(outputFile).use { output ->
                    input.copyTo(output)
                }
            }
            
            Uri.fromFile(outputFile)
        } catch (e: IOException) {
            Log.e("ImageUtils", "Error copying URI to app directory", e)
            null
        }
    }
    
    /**
     * Get file size from URI
     */
    fun getFileSize(context: Context, uri: Uri): Long? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                input.available().toLong()
            }
        } catch (e: Exception) {
            Log.e("ImageUtils", "Error getting file size", e)
            null
        }
    }
    
    /**
     * Check if URI is an image
     */
    fun isImageUri(context: Context, uri: Uri): Boolean {
        return try {
            val mimeType = context.contentResolver.getType(uri)
            mimeType?.startsWith("image/") == true
        } catch (e: Exception) {
            Log.e("ImageUtils", "Error checking MIME type", e)
            false
        }
    }
    
    /**
     * Generate a unique filename with timestamp
     */
    fun generateUniqueFileName(extension: String = "jpg"): String {
        val timestamp = System.currentTimeMillis()
        return "image_$timestamp.$extension"
    }
    
    /**
     * Get readable file size string
     */
    fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
            else -> "${bytes / (1024 * 1024 * 1024)} GB"
        }
    }
}
