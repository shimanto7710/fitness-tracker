package com.bmqa.brac.fitnesstracker.domain.repository

import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse

interface OpenRouterRepository {
    suspend fun analyzeFoodImage(imageBase64: String, customText: String? = null): Result<OpenRouterResponse>
}
