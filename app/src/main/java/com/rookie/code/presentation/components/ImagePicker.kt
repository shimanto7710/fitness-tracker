package com.rookie.code.presentation.components

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import android.content.pm.PackageManager
import com.rookie.code.common.constants.AppConstants
import com.rookie.code.ui.theme.Dimensions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ImagePicker(
    onImageSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showPictureDialog by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var permissionType by remember { mutableStateOf("") }
    
    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // The URI was already set when launching the camera
            // onImageSelected will be called from the callback
        }
    }
    
    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { 
            onImageSelected(it)
        }
    }
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            when (permissionType) {
                "camera" -> launchCamera(context, cameraLauncher) { uri ->
                    onImageSelected(uri)
                }
                "gallery" -> galleryLauncher.launch("image/*")
            }
        } else {
            showPermissionDialog = true
        }
    }
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Picker Button
        Button(
            onClick = {
                showPictureDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.buttonHeight)
                .padding(horizontal = Dimensions.spacingMedium, vertical = Dimensions.spacingSmall),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(Dimensions.borderRadiusMedium)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Select Image",
                modifier = Modifier.size(Dimensions.iconSizeMedium)
            )
            Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
            Text(
                text = AppConstants.UiText.PICK_FOOD_IMAGE,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Picture Selection Dialog
        if (showPictureDialog) {
            PictureSelectionDialog(
                onCameraClick = {
                    showPictureDialog = false
                    if (checkCameraPermission(context)) {
                        launchCamera(context, cameraLauncher) { uri ->
                            onImageSelected(uri)
                        }
                    } else {
                        permissionType = "camera"
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                onGalleryClick = {
                    showPictureDialog = false
                    if (checkStoragePermission(context)) {
                        galleryLauncher.launch("image/*")
                    } else {
                        permissionType = "gallery"
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    }
                },
                onDismiss = { showPictureDialog = false }
            )
        }
        
        // Permission Dialog
        if (showPermissionDialog) {
            PermissionDialog(
                permissionType = permissionType,
                onDismiss = { showPermissionDialog = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PictureSelectionDialog(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Choose Picture Source",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = Dimensions.spacingMedium)
            )
            
            Text(
                text = "Select how you want to get your picture",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = Dimensions.spacingLarge)
            )
            
            // Camera Button
            Button(
                onClick = onCameraClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.buttonHeight),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(Dimensions.borderRadiusMedium)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Camera",
                    modifier = Modifier.size(Dimensions.iconSizeMedium)
                )
                Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
                Text(
                    text = "Take Photo with Camera",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacingMedium))
            
            // Gallery Button
            OutlinedButton(
                onClick = onGalleryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimensions.buttonHeight),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(Dimensions.borderRadiusMedium)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Gallery",
                    modifier = Modifier.size(Dimensions.iconSizeMedium)
                )
                Spacer(modifier = Modifier.width(Dimensions.spacingSmall))
                Text(
                    text = "Choose from Gallery",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
        }
    }
}

@Composable
private fun PermissionDialog(
    permissionType: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permission Required") },
        text = { 
            Text(
                when (permissionType) {
                    "camera" -> "Camera permission is required to take photos. Please grant camera permission in Settings."
                    "gallery" -> "Storage permission is required to access images. Please grant storage permission in Settings."
                    else -> "Permission is required for this feature."
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

// Helper functions
private fun checkCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

private fun checkStoragePermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}

private fun launchCamera(context: Context, launcher: androidx.activity.result.ActivityResultLauncher<Uri>, onUriCreated: (Uri) -> Unit) {
    val photoFile = createImageFile(context)
    photoFile?.let { file ->
        val photoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        onUriCreated(photoUri)
        launcher.launch(photoUri)
    }
}

private fun createImageFile(context: Context): File? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir("Pictures")
    return File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        storageDir
    )
}
