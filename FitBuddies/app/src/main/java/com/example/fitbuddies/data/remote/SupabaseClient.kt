package com.example.fitbuddies.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage


object Supabase {

    private const val BASE_URL = "https://lrvgubyfxybycitbieaf.supabase.co"
    private const val ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6" +
            "Imxydmd1YnlmeHlieWNpdGJpZWFmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzI5MTA1NzksImV4cCI6MjA0ODQ4Nj" +
            "U3OX0.RwYvJiRm8nxrxOZTge1MpkV1pWQyVQqVmMreEp7dSMQ"

    val client = createSupabaseClient(
        supabaseUrl = BASE_URL,
        supabaseKey = ANON_KEY
    ) {
        install(Postgrest)
        install(Storage)
    }
}
