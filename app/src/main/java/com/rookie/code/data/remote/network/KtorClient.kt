package com.rookie.code.data.remote.network

import com.rookie.code.common.constants.AppConstants
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KtorClient {
    
    fun createClarifaiClient(): HttpClient {
        return HttpClient(OkHttp) {
            // Content negotiation for JSON serialization/deserialization
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                    disableHtmlEscaping()
                    setLenient()
                }
            }
            
            // Logging for debugging (only in debug builds)
            install(Logging) {
                level = if (AppConstants.Api.IS_DEBUG) LogLevel.ALL else LogLevel.NONE
                logger = Logger.DEFAULT
            }
            
            // Default request configuration
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.clarifai.com"
                }
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header(HttpHeaders.UserAgent, "FoodLens-Android/1.0")
            }
            
            // Timeout configuration
            install(HttpTimeout) {
                requestTimeoutMillis = AppConstants.Api.REQUEST_TIMEOUT
                connectTimeoutMillis = AppConstants.Api.CONNECT_TIMEOUT
                socketTimeoutMillis = AppConstants.Api.READ_TIMEOUT
            }
            
            // Expect success for better error handling
            expectSuccess = true
            
            // Retry configuration for failed requests
            install(HttpRequestRetry) {
                maxRetries = 3
                exponentialDelay(
                    base = 2.0,
                    maxDelayMs = 10_000L
                )
            }
        }
    }
}
