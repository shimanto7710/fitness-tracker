package com.bmqa.brac.fitnesstracker.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OpenRouterResponse(
    val id: String,
    val provider: String,
    val model: String,
    val `object`: String,
    val created: Long,
    val choices: List<OpenRouterChoice>,
    @SerializedName("system_fingerprint")
    val systemFingerprint: String,
    val usage: OpenRouterUsage
)

data class OpenRouterChoice(
    val logprobs: Any?,
    @SerializedName("finish_reason")
    val finishReason: String,
    @SerializedName("native_finish_reason")
    val nativeFinishReason: String,
    val index: Int,
    val message: OpenRouterChoiceMessage,
    val refusal: Any?,
    val reasoning: Any?
)

data class OpenRouterChoiceMessage(
    val role: String,
    val content: String
)

data class OpenRouterUsage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int,
    @SerializedName("completion_tokens")
    val completionTokens: Int,
    @SerializedName("total_tokens")
    val totalTokens: Int,
    @SerializedName("prompt_tokens_details")
    val promptTokensDetails: Any?
)
