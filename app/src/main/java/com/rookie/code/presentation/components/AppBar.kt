package com.rookie.code.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

/**
 * Shared AppBar component for the entire app
 * Can be customized with different titles and actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    isTransparent: Boolean = false,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = if (isTransparent) TextAlign.Center else TextAlign.Start,
                color = if (isTransparent) Color.White else Color.Unspecified
            )
        },
        navigationIcon = {
            if (showBackButton && onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (isTransparent) Color.White else Color.Unspecified
                    )
                }
            }
        },
        actions = {
            if (isTransparent) {
                // Share button
                IconButton(onClick = { /* Handle share */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }
                // More options button
                IconButton(onClick = { /* Handle more options */ }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.White
                    )
                }
            } else {
                actions?.invoke()
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isTransparent) Color.Transparent else TopAppBarDefaults.topAppBarColors().containerColor,
            titleContentColor = if (isTransparent) Color.White else TopAppBarDefaults.topAppBarColors().titleContentColor,
            navigationIconContentColor = if (isTransparent) Color.White else TopAppBarDefaults.topAppBarColors().navigationIconContentColor,
            actionIconContentColor = if (isTransparent) Color.White else TopAppBarDefaults.topAppBarColors().actionIconContentColor
        ),
        modifier = modifier
    )
}
