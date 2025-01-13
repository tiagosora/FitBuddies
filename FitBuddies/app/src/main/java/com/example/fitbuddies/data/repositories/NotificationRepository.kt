package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.models.Dare
import com.example.fitbuddies.data.models.Notification
import com.example.fitbuddies.data.remote.Supabase.client
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.Json

class NotificationRepository {

    suspend fun getNotificationById(notificationId: String): Notification? {
        val response = client.from("notifications").select {
            filter {
                eq("notificationid", notificationId)
            }
        }

        try {
            val decodedResponse: List<Notification> = Json.decodeFromString(response.data)
            val user : Notification? = decodedResponse.firstOrNull()
//            println("Decoded Notification: ${decodedResponse.firstOrNull()}")
            return user
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return null
        }
    }

    suspend fun getUsersToNotifyByChallenge(challengeId: String): List<String> {
        try {
            // Query to fetch all dares associated with the given challengeId
            val response = client.from("dares").select {
                filter {
                    eq("challengeid", challengeId)
                }
            }

            // Deserialize response into a list of Dare objects
            val dares: List<Dare> = Json.decodeFromString(response.data)

            // Extract all "daredToId" values from the list of dares
            return dares.map { it.daredToId }
        } catch (e: Exception) {
            println("Error fetching dares for challenge $challengeId: ${e.message}")
            return emptyList()
        }
    }

}