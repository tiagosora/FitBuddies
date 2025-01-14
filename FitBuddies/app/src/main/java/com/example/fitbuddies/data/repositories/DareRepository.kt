package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.models.Dare
import com.example.fitbuddies.data.models.Challenge
import com.example.fitbuddies.viewmodels.NotificationsViewModel
import com.example.fitbuddies.data.remote.Supabase.client
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.math.min

class DareRepository @Inject constructor(
    private val notificationsViewModel: NotificationsViewModel
) {
    suspend fun createDare(dare: Dare) {
        try {
            client.from("dares").insert(dare)

            val response = client.from("challenges").select {
                filter {
                    eq("challengeid", dare.challengeId)
                }
            }

            val challenges: List<Challenge> = Json.decodeFromString(response.data)
            val challengeTitle = challenges.firstOrNull()?.title ?: "Unknown Challenge"

            notificationsViewModel.addNotification(
                challengeId = dare.challengeId,
                title = "You were challenged to: $challengeTitle",
                description = "See more information about the challenge \"$challengeTitle\".",
                status = "Pending"
            )
        } catch (e: Exception) {
            println("Error creating dare: ${e.message}")
        }
    }

    suspend fun getDaredByChallengeId(challengeId: String): List<Dare> {
        val response = client.from("dares").select {
            filter {
                and {
                    eq("challengeid", challengeId)
                    eq("isaccepted", true)
                }
            }
        }
        try {
            val decodedDares: List<Dare> = Json.decodeFromString(response.data)
            return decodedDares
        } catch (e: Exception) {
            println("Error getting dared users: ${e.message}")
            return emptyList()
        }
    }

    suspend fun updateDareCompletion(challengeID: String, challengeGoal: Int, userId: String, completion: Long) {
        val response = client.from("dares").select {
            filter {
                and {
                    eq("challengeid", challengeID)
                    eq("daredtoid", userId)
                }
            }
        }
        try {
            val decodedDares: List<Dare> = Json.decodeFromString(response.data)
            println("Decoded Dares: $decodedDares")
            val dare = decodedDares.firstOrNull()
            println("Dare: $dare")
            if (dare != null) {
                var num : Number = (completion.toDouble().div(60).div(60)).div(challengeGoal).times(100).plus(dare.completionRate)
                println("Completion: $num")
                val updatedDare = dare.copy(completionRate = min(num.toInt(), 100))
                println("Updated Dare: $updatedDare")
                client.from("dares").upsert(updatedDare)
            }
        } catch (e: Exception) {
            println("Error updating dare completion: ${e.message}")
        }
    }
}
