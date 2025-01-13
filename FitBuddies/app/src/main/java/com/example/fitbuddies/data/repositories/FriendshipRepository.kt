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
import javax.inject.Inject

class FriendshipRepository @Inject constructor() {

    suspend fun getUserFriendshipRequests(userId: String): List<RequestedFriendshipResponse> {
        val columns = Columns.raw(
            """
            users:userid (
                userid,
                firstname,
                lastname,
                profilepictureurl
            ),
            creationdate
            """.trimIndent()
        )
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

        return try {
            Json.decodeFromString(response.data)
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            emptyList()
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

        return try {
            Json.decodeFromString(response.data)
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            emptyList()
        }
    }

    suspend fun getFitBuddiesDetailsWithCountChallenges(userId: String): List<FitBuddyCountChallenges> {
        return try {
            val response = client.postgrest.rpc(
                function = "get_fitbuddies_with_completed_challenges",
                parameters = mapOf("user_id" to userId)
            )

            Json.decodeFromString(response.data)
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            emptyList()
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
