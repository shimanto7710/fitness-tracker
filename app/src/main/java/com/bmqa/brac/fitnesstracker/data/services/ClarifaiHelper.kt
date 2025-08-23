package com.bmqa.brac.fitnesstracker.data.services

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ClarifaiHelper {

    private val client = OkHttpClient()
    private val pat = "57aee404b0c44eddb317dc69f04895b5" // Your Clarifai Personal Access Token
    private val modelUrl = "https://api.clarifai.com/v2/models/food-item-recognition/outputs"

    fun predictImage(imageUrl: String, callback: (String) -> Unit) {
        val jsonBody = """
            {
              "user_app_id": {
                "user_id": "clarifai",
                "app_id": "main"
              },
              "inputs": [
                {
                  "data": {
                    "image": {
                      "url": "$imageUrl"
                    }
                  }
                }
              ]
            }
        """.trimIndent()

        val body = jsonBody.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(modelUrl)
            .addHeader("Authorization", "Key $pat")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                android.util.Log.e("ClarifaiHelper", "Network error: ${e.message}", e)
                callback("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                android.util.Log.d("ClarifaiHelper", "Response code: ${response.code}")
                android.util.Log.d("ClarifaiHelper", "Response headers: ${response.headers}")
                
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: "No error body"
                    android.util.Log.e("ClarifaiHelper", "API error: ${response.code} - $errorBody")
                    callback("API Error: ${response.code} - $errorBody")
                    return
                }
                
                val json = response.body?.string() ?: "{}"
                android.util.Log.d("ClarifaiHelper", "Response body: $json")
                
                try {
                    val jsonObj = JSONObject(json)
                    val concepts = jsonObj
                        .getJSONArray("outputs")
                        .getJSONObject(0)
                        .getJSONObject("data")
                        .getJSONArray("concepts")

                    val results = StringBuilder()
                    for (i in 0 until concepts.length()) {
                        val name = concepts.getJSONObject(i).getString("name")
                        val value = concepts.getJSONObject(i).getDouble("value")
                        results.append("$name: $value\n")
                    }

                    android.util.Log.d("ClarifaiHelper", "Parsed results: $results")
                    callback(results.toString())
                } catch (e: Exception) {
                    android.util.Log.e("ClarifaiHelper", "Error parsing response", e)
                    callback("Error parsing response: ${e.message}")
                }
            }
        })
    }
    
    fun predictImageBase64(base64Image: String, callback: (String) -> Unit) {
        val jsonBody = """
            {
              "user_app_id": {
                "user_id": "clarifai",
                "app_id": "main"
              },
              "inputs": [
                {
                  "data": {
                    "image": {
                      "base64": "$base64Image"
                    }
                  }
                }
              ]
            }
        """.trimIndent()

        val body = jsonBody.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(modelUrl)
            .post(body)
            .addHeader("Authorization", "Key $pat")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                android.util.Log.e("ClarifaiHelper", "Network error: ${e.message}", e)
                callback("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                android.util.Log.d("ClarifaiHelper", "Response code: ${response.code}")
                android.util.Log.d("ClarifaiHelper", "Response headers: ${response.headers}")
                
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: "No error body"
                    android.util.Log.e("ClarifaiHelper", "API error: ${response.code} - $errorBody")
                    callback("API Error: ${response.code} - $errorBody")
                    return
                }
                
                val json = response.body?.string() ?: "{}"
                android.util.Log.d("ClarifaiHelper", "Response body: $json")
                
                try {
                    val jsonObj = JSONObject(json)
                    val concepts = jsonObj
                        .getJSONArray("outputs")
                        .getJSONObject(0)
                        .getJSONObject("data")
                        .getJSONArray("concepts")

                    val results = StringBuilder()
                    for (i in 0 until concepts.length()) {
                        val name = concepts.getJSONObject(i).getString("name")
                        val value = concepts.getJSONObject(i).getDouble("value")
                        results.append("$name: $value\n")
                    }

                    android.util.Log.d("ClarifaiHelper", "Parsed results: $results")
                    callback(results.toString())
                } catch (e: Exception) {
                    android.util.Log.e("ClarifaiHelper", "Error parsing response", e)
                    callback("Error parsing response: ${e.message}")
                }
            }
        })
    }
}
