package com.example.fitbuddies.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object SupabaseClient {

    private const val BASE_URL = "https://lrvgubyfxybycitbieaf.supabase.co"
    private const val ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6" +
            "Imxydmd1YnlmeHlieWNpdGJpZWFmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzI5MTA1NzksImV4cCI6MjA0ODQ4Nj" +
            "U3OX0.RwYvJiRm8nxrxOZTge1MpkV1pWQyVQqVmMreEp7dSMQ"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("apikey", ANON_KEY)
                .addHeader("Authorization", ANON_KEY)
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val instance: SupabaseService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseService::class.java)
    }
}
