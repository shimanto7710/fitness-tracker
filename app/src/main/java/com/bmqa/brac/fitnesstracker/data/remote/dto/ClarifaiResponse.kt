package com.bmqa.brac.fitnesstracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ClarifaiResponse(
    val status: ClarifaiStatus,
    val outputs: List<ClarifaiOutput>
)

data class ClarifaiStatus(
    val code: Int,
    val description: String
)

data class ClarifaiOutput(
    val id: String,
    val status: ClarifaiStatus,
    val created_at: String,
    val model: ClarifaiModel,
    val input: ClarifaiInput,
    val data: ClarifaiData
)

data class ClarifaiModel(
    val id: String,
    val name: String,
    val created_at: String,
    val app_id: String,
    val output_info: ClarifaiOutputInfo,
    val model_version: ClarifaiModelVersion
)

data class ClarifaiOutputInfo(
    val output_config: ClarifaiOutputConfig
)

data class ClarifaiOutputConfig(
    val select_concepts: List<ClarifaiConcept>
)

data class ClarifaiConcept(
    val id: String,
    val name: String,
    val value: Float
)

data class ClarifaiModelVersion(
    val id: String,
    val created_at: String,
    val status: ClarifaiStatus
)

data class ClarifaiInput(
    val id: String,
    val data: ClarifaiInputData
)

data class ClarifaiInputData(
    val image: ClarifaiImage
)

data class ClarifaiImage(
    val url: String? = null,
    val base64: String? = null
)

data class ClarifaiData(
    val concepts: List<ClarifaiConcept>
)
