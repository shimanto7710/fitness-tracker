package com.bmqa.brac.fitnesstracker.data.repository

import com.bmqa.brac.fitnesstracker.data.remote.datasource.GeminiFoodAnalysisDataSource
import com.bmqa.brac.fitnesstracker.domain.entities.GeminiFoodAnalysis
import com.bmqa.brac.fitnesstracker.domain.repository.GeminiFoodAnalysisRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiFoodAnalysisRepositoryImpl @Inject constructor(
    private val dataSource: GeminiFoodAnalysisDataSource
) : GeminiFoodAnalysisRepository {
    
    override suspend fun analyzeFoodWithGemini(imageUri: String, base64Image: String): Result<GeminiFoodAnalysis> {
        return dataSource.analyzeFoodWithGemini(imageUri, base64Image)
    }
}

