package com.bmqa.brac.fitnesstracker.domain.repository

import android.net.Uri
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse

interface OpenRouterRepository {
    suspend fun analyzeFoodImage(imageUri: Uri, customText: String? = null): Result<OpenRouterResponse>
}
