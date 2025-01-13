package com.example.fitbuddies.ai;

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class ChatGPTRequest(val model: String, val messages: List<Map<String, String>>)
data class ChatGPTResponse(val choices: List<Choice>)
data class Choice(val message: Map<String, String>)

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    fun sendMessage(@Body body: ChatGPTRequest): Call<ChatGPTResponse>
}



