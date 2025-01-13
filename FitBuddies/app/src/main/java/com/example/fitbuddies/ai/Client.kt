
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



    fun generateFitnessChallenge(challengeType : String): Map<String, Any>? {
        val apiService = create()

        val request = ChatGPTRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(
                mapOf("role" to "system", "content" to "You are a fitness expert."),
                mapOf("role" to "user", "content" to "According to the challenge type ($challengeType), generate a fitness challenge in JSON format. The format must be {title: challenge_title, description: challenge_description}. You must only respond with that json format. You must only respond in that JSON format. The description must not exceed 200 characters."),
                mapOf("role" to "user", "content" to "The exercise must be concluded by a normal person within a normal timeframe."),
            )
        )

        var returnValue: Map<String, Any>? = null

        apiService.sendMessage(request).enqueue(object : Callback<ChatGPTResponse> {
            override fun onResponse(call: Call<ChatGPTResponse>, response: Response<ChatGPTResponse>) {
                if (response.isSuccessful) {
                    response.body()?.choices?.get(0)?.message?.get("content")?.let { content ->
                        try {
                            returnValue = parseFitnessChallenge(content)
                            println("Parsed Challenge: $returnValue")
                        } catch (e: Exception) {
                            println("Failed to parse challenge: ${e.message}")
                        }
                    }
                } else {
                    println("Error: ${response.errorBody()?.string()}")
                }
            }


            override fun onFailure(call: Call<ChatGPTResponse>, t: Throwable) {
                println("Failed to communicate: ${t.message}")
            }
        })

        return returnValue
        println("Generated Challenge: $returnValue")
    }


}
