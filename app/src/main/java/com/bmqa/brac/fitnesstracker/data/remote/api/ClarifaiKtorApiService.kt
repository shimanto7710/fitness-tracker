package com.bmqa.brac.fitnesstracker.data.remote.api

import com.bmqa.brac.fitnesstracker.common.constants.AppConstants
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiRequest
import com.bmqa.brac.fitnesstracker.data.remote.dto.ClarifaiResponse
import com.bmqa.brac.fitnesstracker.data.remote.network.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClarifaiKtorApiService @Inject constructor(
    private val httpClient: HttpClient
) {
    
    suspend fun recognizeFood(request: ClarifaiRequest): ClarifaiResponse = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = httpClient.post("v2/models/${AppConstants.Api.MODEL_ID}/outputs") {
                header(HttpHeaders.Authorization, AppConstants.Api.AUTHORIZATION_HEADER)
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            // Check if response is successful
            if (!response.status.isSuccess()) {
                val errorBody = response.bodyAsText()
                throw ApiException(
                    statusCode = response.status,
                    responseBody = errorBody,
                    message = "Clarifai API request failed: ${response.status.value} - $errorBody"
                )
            }
            
            // Parse response body
            val responseBody = response.body<ClarifaiResponse>()
            responseBody ?: throw EmptyResponseException("Empty response from Clarifai API")
            
        } catch (e: ApiException) {
            throw e // Re-throw API exceptions
        } catch (e: JsonParsingException) {
            throw e // Re-throw JSON parsing exceptions
        } catch (e: Exception) {
            // Handle other exceptions
            when {
                e.message?.contains("timeout", ignoreCase = true) == true -> 
                    throw TimeoutException("Request to Clarifai API timed out")
                e.message?.contains("network", ignoreCase = true) == true -> 
                    throw NoInternetException("Network error while calling Clarifai API")
                else -> 
                    throw UnknownNetworkException("Unknown error occurred while calling Clarifai API", e)
            }
        }
    }
}
