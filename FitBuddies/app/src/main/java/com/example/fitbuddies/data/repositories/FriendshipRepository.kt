package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.models.Friendship
import com.example.fitbuddies.data.remote.Supabase.client
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


class FriendshipRepository() {

    suspend fun getUserFriendshipRequests(userId: String): List<RequestedFriendshipResponse> {
        val columns = Columns.raw("""
            users:userid (
                userid,
                firstname,
                lastname,
                profilepictureurl
            ),
            creationdate
        """.trimIndent())
        val response = client.from("friendships").select(
            columns = columns
        ) {
            filter {
                and {
                    eq("friendid", userId)
                    eq("isaccepted", false)
                }
            }
            order(column = "creationdate", order = Order.ASCENDING)
        }

        try {
            val decodedResponse: List<RequestedFriendshipResponse> = Json.decodeFromString(response.data)
//            println("Decoded Friendship Requests: $decodedResponse")
            return decodedResponse
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getUserAcceptedFriendships(userId: String): List<Friendship> {
        val response = client.from("friendships").select {
            filter {
                and {
                    or {
                        eq("userid", userId)
                        eq("friendid", userId)
                    }
                    eq("isaccepted", true)
                }
            }
        }

        try {
            val decodedResponse: List<Friendship> = Json.decodeFromString(response.data)
//            println("Decoded User Friendships: $decodedResponse")
            return decodedResponse
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getFitBuddiesDetailsWitCountChallenges(userId: String): List<FitBuddyCountChallenges> {
        try {
            val response = client.postgrest.rpc(
                // Used a stored Procedure
                function = "get_fitbuddies_with_completed_challenges",
                parameters = mapOf("user_id" to userId)
            )

//            println("Requested Response: ${response.data}")
            val decodedResponse: List<FitBuddyCountChallenges> = Json.decodeFromString(response.data)
//            println("Decoded Requested Response: $decodedResponse")
            return decodedResponse
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return emptyList()
        }
    }

    @Serializable
    data class FitBuddyCountChallenges(
        val fitbuddyid: String,
        val firstname: String,
        val lastname: String,
        val profilepictureurl: String,
        val challengescompletedcount: Int
    )

    @Serializable
    data class UserDetails(
        val userid: String,
        val firstname: String,
        val lastname: String,
        val profilepictureurl: String
    )

    @Serializable
    data class RequestedFriendshipResponse(
        val users: UserDetails,
        val creationdate: String
    )
}
