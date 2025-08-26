package com.bmqa.brac.fitnesstracker.data.repository

import android.content.Context
import android.net.Uri
import com.bmqa.brac.fitnesstracker.data.remote.datasource.OpenRouterRemoteDataSource
import com.bmqa.brac.fitnesstracker.data.remote.dto.OpenRouterResponse
import com.bmqa.brac.fitnesstracker.domain.repository.OpenRouterRepository
import javax.inject.Inject

class OpenRouterRepositoryImpl @Inject constructor(
    private val remoteDataSource: OpenRouterRemoteDataSource,
    private val context: Context
) : OpenRouterRepository {
    
    override suspend fun analyzeFoodImage(imageUri: Uri, customText: String?): Result<OpenRouterResponse> {
        return try {
            remoteDataSource.analyzeFood(imageUri, customText, context)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
