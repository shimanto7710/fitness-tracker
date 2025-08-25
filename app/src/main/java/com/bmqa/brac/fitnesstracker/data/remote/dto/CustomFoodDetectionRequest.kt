package com.bmqa.brac.fitnesstracker.data.remote.dto

import okhttp3.MultipartBody

data class CustomFoodDetectionRequest(
    val imageFile: MultipartBody.Part
)
