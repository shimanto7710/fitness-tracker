package com.bmqa.brac.foodlens.data.remote.network

import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * Base exception for all network-related errors
 */
sealed class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Exception thrown when API returns an error response
 */
class ApiException(
    val statusCode: HttpStatusCode,
    val responseBody: String? = null,
    message: String = "API request failed with status: ${statusCode.value}"
) : NetworkException(message)

/**
 * Exception thrown when network request times out
 */
class TimeoutException(message: String = "Request timed out") : NetworkException(message)

/**
 * Exception thrown when there's no internet connection
 */
class NoInternetException(message: String = "No internet connection") : NetworkException(message)

/**
 * Exception thrown when JSON parsing fails
 */
class JsonParsingException(message: String = "Failed to parse JSON response", cause: Throwable? = null) : NetworkException(message, cause)

/**
 * Exception thrown when the response is empty or null
 */
class EmptyResponseException(message: String = "Empty response from server") : NetworkException(message)

/**
 * Exception thrown for unknown network errors
 */
class UnknownNetworkException(message: String = "Unknown network error", cause: Throwable? = null) : NetworkException(message, cause)
