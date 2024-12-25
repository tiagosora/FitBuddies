package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.models.Challenge
import com.example.fitbuddies.data.local.ChallengeDao
import kotlinx.coroutines.flow.Flow
import android.util.Log
import com.example.fitbuddies.data.remote.Supabase.client
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.json.Json

class ChallengeRepository(
    private val challengeDao: ChallengeDao,
) {

    suspend fun getChallengeById(challengeId: String): Flow<Challenge?> = challengeDao.getChallengeById(challengeId)

    fun getChallengesForUser(userId: String): Flow<List<Challenge>> = challengeDao.getChallengesForUser(userId)

    suspend fun insertChallenge(challenge: Challenge) {
        challengeDao.insertChallenge(challenge) // Save locally first
        try {
            // supabaseService.insertChallenge(challenge) // Sync to Supabase
        } catch (e: Exception) {
            Log.e("ChallengeRepository", "Failed to sync challenge: ${challenge.challengeId}", e)
        }
    }

    suspend fun getActiveUserChallenges(userId: String): List<Map<String, Any>> {

        val columns = Columns.raw("""
            dare_id,
            is_accepted,
            is_completed,
            completion_date,
            completion_rate,
            challenges:challenge_id (
                challenge_id,
                title,
                description,
                type,
                dared_by_id,
                creation_date,
                deadline_date
            )
        """.trimIndent())
        val response = client.from("dares").select(
            columns = columns
        ) {
            filter {
                eq("dared_by_id", userId)
                eq("is_accepted", true)
                eq("is_completed", false)
            }
        }

        try {
            val decodedResponse: List<Map<String, Any>> = Json.decodeFromString(response.data.toString())
            println("Decoded Active User Challenges: $decodedResponse")
            return decodedResponse
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getRequestedUserChallenges(userId: String): List<Map<String, Any>> {

        val columns = Columns.raw("""
            dare_id,
            is_accepted,
            is_completed,
            completion_date,
            completion_rate,
            challenges:challenge_id (
                challenge_id,
                title,
                description,
                type,
                dared_by_id,
                creation_date,
                deadline_date
            )
        """.trimIndent())
        val response = client.from("dares").select(
            columns = columns
        ) {
            filter {
                eq("dared_by_id", userId)
                eq("is_accepted", false)
                eq("is_completed", false)
            }
        }

        try {
            val decodedResponse: List<Map<String, Any>> = Json.decodeFromString(response.data.toString())
            println("Decoded Requested User Challenges: $decodedResponse")
            return decodedResponse
        } catch (e: Exception) {
            println("Deserialization Error: ${e.message}")
            return emptyList()
        }
    }


    suspend fun deleteChallenge(challenge: Challenge) {
        challengeDao.deleteChallenge(challenge) // Remove locally
        try {
            //  supabaseService.deleteChallenge(challenge.challengeId) // Delete remotely
        } catch (e: Exception) {
            Log.e("ChallengeRepository", "Failed to delete challenge: ${challenge.challengeId}", e)
        }
    }
}
