package com.bmqa.brac.fitnesstracker.data.repository

import android.content.Context
import android.net.Uri
import com.bmqa.brac.fitnesstracker.data.remote.datasource.GeminiFoodAnalysisDataSource
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import javax.inject.Inject

interface GeminiFoodAnalysisRepository {
    suspend fun analyzeFoodWithGemini(imageUri: Uri, context: Context): Result<GeminiFoodAnalysis>
}

class GeminiFoodAnalysisRepositoryImpl @Inject constructor(
    private val dataSource: GeminiFoodAnalysisDataSource
) : GeminiFoodAnalysisRepository {
    
    override suspend fun analyzeFoodWithGemini(imageUri: Uri, context: Context): Result<GeminiFoodAnalysis> {
        return dataSource.analyzeFoodWithGemini(imageUri, context)
    }
}

