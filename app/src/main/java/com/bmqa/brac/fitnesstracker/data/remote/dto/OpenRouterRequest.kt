package com.bmqa.brac.fitnesstracker.data.remote.dto

data class OpenRouterRequest(
    val model: String = "qwen/qwen-2.5-vl-7b-instruct",
    val messages: List<OpenRouterMessage>
)

data class OpenRouterMessage(
    val role: String = "user",
    val content: List<OpenRouterContent>
)

sealed class OpenRouterContent {
    data class TextContent(
        val type: String = "text",
        val text: String
    ) : OpenRouterContent()
    
    data class ImageContent(
        val type: String = "image_base64",
        val image_base64: String
    ) : OpenRouterContent()
}
