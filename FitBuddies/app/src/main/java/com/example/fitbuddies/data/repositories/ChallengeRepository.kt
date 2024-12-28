package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.remote.Supabase.client
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class ChallengeRepository() {
    suspend fun getActiveUserChallenges(userId: String): List<DareToChallengeResponse> {

        val columns = Columns.raw("""
            dareid,
            isaccepted,
            iscompleted,
            completiondate,
            completionrate,
            challenges:challengeid (
                challengeid,
                title,
                description,
                type,
                daredbyid,
                creationdate,
                deadlinedate
            )
        """.trimIndent())
        val response = client.from("dares").select(
            columns = columns
        ) {
            filter {
                and {
                    eq("daredtoid", userId)
                    eq("isaccepted", true)
                    eq("iscompleted", false)
                }
            }
        }

        try {
//            println("Requested Challenges Response: ${response.data}")
            val decodedResponse: List<DareToChallengeResponse> = Json.decodeFromString(response.data)
//            println("Decoded Active Challenges: $decodedResponse")
            return decodedResponse
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getRequestedUserChallenges(userId: String): List<DareToChallengeResponse> {

        val columns = Columns.raw("""
            dareid,
            isaccepted,
            iscompleted,
            completiondate,
            completionrate,
            challenges:challengeid (
                challengeid,
                title,
                description,
                type,
                daredbyid,
                creationdate,
                deadlinedate
            )
        """.trimIndent())
        val response = client.from("dares").select(
            columns = columns
        ) {
            filter {
                and {
                    eq("daredtoid", userId)
                    eq("isaccepted", false)
                }
            }
        }

        try {
//            println("Requested Challenges Response: ${response.data}")
            val decodedResponse: List<DareToChallengeResponse> = Json.decodeFromString(response.data)
//            println("Decoded Requested Challenges: $decodedResponse")
            return decodedResponse
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getUserCompletedChallenges(userId: String): List<DareToChallengeResponse> {

        val columns = Columns.raw("""
            dareid,
            isaccepted,
            iscompleted,
            completiondate,
            completionrate,
            challenges:challengeid (
                challengeid,
                title,
                description,
                type,
                daredbyid,
                creationdate,
                deadlinedate
            )
        """.trimIndent())
        val response = client.from("dares").select(
            columns = columns
        ) {
            filter {
                and {
                    eq("daredtoid", userId)
                    eq("iscompleted", true)
                }
            }
        }

        try {
//            println("Requested Challenges Response: ${response.data}")
            val decodedResponse: List<DareToChallengeResponse> = Json.decodeFromString(response.data)
//            println("Decoded Requested Challenges: $decodedResponse")
            return decodedResponse
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getFitBuddiesChallenges(userFitBuddiesIds: List<String>): List<FitBuddyLastCompletedChallenge> {

        try {
            val response = client.postgrest.rpc(
                // Used a stored Procedure
                function = "get_last_completed_challenges",
                parameters = mapOf("fitbuddies_ids" to userFitBuddiesIds)
            )
//            println("Requested Challenges Response: ${response.data}")
            val decodedResponse: List<FitBuddyLastCompletedChallenge> = Json.decodeFromString(response.data)
//            println("Decoded Requested Challenges: $decodedResponse")
            return decodedResponse
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return emptyList()
        }
    }

    @Serializable
    data class ChallengeDetails(
        val challengeid: String = "",
        val title: String = "",
        val description: String = "",
        val type: String = "",
        val daredbyid: String = "",
        val creationdate: String = "",
        val deadlinedate: String = ""
    )

    @Serializable
    data class DareToChallengeResponse(
        val dareid: String = "",
        val isaccepted: Boolean = false,
        val iscompleted: Boolean = false,
        val completiondate: String = "",
        val completionrate: Float = 0.0f,
        val challenges: ChallengeDetails = ChallengeDetails()
    )

    @Serializable
    data class FitBuddyLastCompletedChallenge(
        val dareid: String,
        val daredtoid: String,
        val isaccepted: Boolean,
        val iscompleted: Boolean,
        val completiondate: String,
        val completionrate: Float,
        val challengeid: String,
        val title: String,
        val description: String,
        val type: String,
        val daredbyid: String,
        val creationdate: String,
        val deadlinedate: String
    )
}

