package com.bmqa.brac.fitnesstracker.data.remote.dto

data class ClarifaiRequest(
    val user_app_id: ClarifaiUserAppId,
    val inputs: List<ClarifaiInputRequest>
)

data class ClarifaiUserAppId(
    val user_id: String,
    val app_id: String
)

data class ClarifaiInputRequest(
    val data: ClarifaiInputDataRequest
)

data class ClarifaiInputDataRequest(
    val image: ClarifaiImageRequest
)

data class ClarifaiImageRequest(
    val base64: String
)
