package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.models.User
import com.example.fitbuddies.data.remote.Supabase.client
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.Json

class UserRepository() {
    suspend fun getUserById(userId: String): User? {
        val response = client.from("users").select {
            filter {
                eq("userid", userId)
            }
        }

        try {
            val decodedResponse: List<User> = Json.decodeFromString(response.data)
            val user : User? = decodedResponse.firstOrNull()
//            println("Decoded User: ${decodedResponse.firstOrNull()}")
            return user
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return null
        }
    }

    suspend fun getUsersByIds(userIds: List<String>): List<User> {
        val response = client.from("users").select {
            filter {
                isIn("userid", userIds)
            }
        }

        try {
            val decodedResponse: List<User> = Json.decodeFromString(response.data)
//            println("Decoded Users: ${decodedResponse}")
            return decodedResponse
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun authenticateUser(email: String, password: String): User? {
        val response = client.from("users").select {
            filter {
                and {
                    eq("email", email)
                    eq("password", password)
                }
            }
        }

        try {
            val decodedResponse: List<User> = Json.decodeFromString(response.data)
            val user : User? = decodedResponse.firstOrNull()
//            println("Decoded User: ${decodedResponse.firstOrNull()}")
            return user
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return null
        }
    }

    suspend fun insertUser(user: User): User? {
        val response = client.from("users").insert(user) {
            select()
        }

        try {
            val decodedResponse: List<User> = Json.decodeFromString(response.data.toString())
            val user : User? = decodedResponse.firstOrNull()
//            println("Decoded User: ${decodedResponse.firstOrNull()}")
            return user
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return null
        }
    }

}
