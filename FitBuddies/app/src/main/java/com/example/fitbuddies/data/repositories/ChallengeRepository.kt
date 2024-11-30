package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.models.Challenge
import com.example.fitbuddies.data.local.ChallengeDao
import com.example.fitbuddies.data.remote.SupabaseService
import kotlinx.coroutines.flow.Flow
import android.util.Log

class ChallengeRepository(
    private val challengeDao: ChallengeDao,
    private val supabaseService: SupabaseService
) {

    fun getChallengesForUser(userId: String): Flow<List<Challenge>> = challengeDao.getChallengesForUser(userId)

    suspend fun insertChallenge(challenge: Challenge) {
        challengeDao.insertChallenge(challenge) // Save locally first
        try {
            supabaseService.insertChallenge(challenge) // Sync to Supabase
        } catch (e: Exception) {
            Log.e("ChallengeRepository", "Failed to sync challenge: ${challenge.challengeId}", e)
        }
    }

    suspend fun refreshChallenges() {
        try {
            val remoteChallenges = supabaseService.getAllChallenges()
            remoteChallenges.forEach { challengeDao.insertChallenge(it) } // Update local cache
        } catch (e: Exception) {
            Log.e("ChallengeRepository", "Failed to refresh challenges", e)
        }
    }

    suspend fun deleteChallenge(challenge: Challenge) {
        challengeDao.deleteChallenge(challenge) // Remove locally
        try {
            supabaseService.deleteChallenge(challenge.challengeId) // Delete remotely
        } catch (e: Exception) {
            Log.e("ChallengeRepository", "Failed to delete challenge: ${challenge.challengeId}", e)
        }
    }
}