
package com.example.fitbuddies.ai

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AiClient {
    private const val BASE_URL = "https://api.openai.com/"
    private const val API_KEY = "sk-TYWh_w8cpbW-0IJWZKOWA_332MxHfsiJoTfiBonRo5T3BlbkFJEB10TM0nt5PxHjNl5nFxA8DSuwKIJg9k1a2bMRPYUA"

    fun create(): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $API_KEY")
                    .addHeader("Content-Type", "application/json")
                    .build()
                println("Request Headers: ${request.headers}")
                chain.proceed(request)
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun parseFitnessChallenge(json: String): Map<String, Any> {
        val type = object : TypeToken<Map<String, Any>>() {}.type
        return Gson().fromJson(json, type)
    }



    fun generateFitnessChallenge(
        challengeType: String,
        callback: (Map<String, Any>?) -> Unit
    ) {
        val apiService = create()

        val request = ChatGPTRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(
                mapOf("role" to "system", "content" to "You are a fitness expert."),
                mapOf(
                    "role" to "user", "content" to """
                According to the challenge type ($challengeType), generate a fitness challenge in JSON format. 
                The format must be {title: challenge_title, description: challenge_description, duration: duration_in_days, goal: challenge_goal}. 
                You must only respond with that JSON format. 
                The description must not exceed 200 characters. 
                Duration (days) must be a single integer (not Float or Double, ex. 28.0 or 28.00 or 28.000). 
                Goal must be a single integer (not Float or Double) representing Km (when challenge type is Running) or Hours otherwise. 
                The exercise must be concluded by a normal person within a normal timeframe.
            """.trimIndent()
                )
            )
        )

        apiService.sendMessage(request).enqueue(object : Callback<ChatGPTResponse> {
            override fun onResponse(call: Call<ChatGPTResponse>, response: Response<ChatGPTResponse>) {
                if (response.isSuccessful) {
                    response.body()?.choices?.get(0)?.message?.get("content")?.let { content ->
                        try {
                            val parsedChallenge = parseFitnessChallenge(content)
                            println("Parsed Challenge: $parsedChallenge")
                            callback(parsedChallenge) // Pass the result to the callback
                        } catch (e: Exception) {
                            println("Failed to parse challenge: ${e.message}")
                            callback(null)
                        }
                    } ?: callback(null) // Handle null content
                } else {
                    println("Error: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ChatGPTResponse>, t: Throwable) {
                println("Failed to communicate: ${t.message}")
                callback(null)
            }
        })
    }


}
