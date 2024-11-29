package com.example.fitbuddies.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object SupabaseClient {

    private const val BASE_URL = "https://your-supabase-url.supabase.co/rest/v1/" // TODO: Replace with Supabase URL

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("apikey", "your-supabase-anon-key") // TODO: Replace with Supabase anon key
                .addHeader("Authorization", "Bearer your-supabase-anon-key") // TODO: Replace with Supabase bearer token
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
