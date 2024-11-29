package com.example.fitbuddies.repositories

import com.example.fitbuddies.models.ChallengeMedia
import com.example.fitbuddies.local.ChallengeMediaDao
import com.example.fitbuddies.remote.SupabaseService
import kotlinx.coroutines.flow.Flow
import android.util.Log

class ChallengeMediaRepository(
    private val challengeMediaDao: ChallengeMediaDao,
    private val supabaseService: SupabaseService
) {

    fun getMediaByDare(dareId: String): Flow<List<ChallengeMedia>> = challengeMediaDao.getMediaByDare(dareId)

    suspend fun insertMedia(media: ChallengeMedia) {
        challengeMediaDao.insertMedia(media) // Save locally first
        try {
            supabaseService.insertMedia(media) // Sync to Supabase
        } catch (e: Exception) {
            Log.e("ChallengeMediaRepository", "Failed to sync media", e)
        }
    }

    suspend fun deleteMedia(media: ChallengeMedia) {
        challengeMediaDao.deleteMedia(media) // Remove locally
        try {
            supabaseService.deleteMedia(media.mediaId) // Delete remotely
        } catch (e: Exception) {
            Log.e("ChallengeMediaRepository", "Failed to delete media: ${media.mediaId}", e)
        }
    }
}
