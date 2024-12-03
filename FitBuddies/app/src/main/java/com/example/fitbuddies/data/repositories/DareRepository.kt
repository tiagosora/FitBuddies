package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.models.Dare
import com.example.fitbuddies.data.local.DareDao
import com.example.fitbuddies.data.remote.SupabaseService
import kotlinx.coroutines.flow.Flow
import android.util.Log

class DareRepository(
    private val dareDao: DareDao,
    private val supabaseService: SupabaseService
) {

    suspend fun insertDare(dare: Dare) {
        dareDao.insertDare(dare) // Save locally first
        try {
            supabaseService.insertDare(dare) // Sync to Supabase
        } catch (e: Exception) {
            Log.e("DareRepository", "Failed to sync dare: ${dare.dareId}", e)
        }
    }

    // acceptDare using challengeId and userId
    suspend fun acceptDare(challengeId: String, userId: String) {
        dareDao.acceptDare(challengeId, userId) // Update locally
        try {
            supabaseService.acceptDare(challengeId, userId) // Sync to Supabase
        } catch (e: Exception) {
            Log.e("DareRepository", "Failed to accept dare: $challengeId", e)
        }
    }

    suspend fun refreshDares() {
        // TODO: Implement this method
    }

    suspend fun deleteDare(dare: Dare) {
        dareDao.deleteDare(dare) // Remove locally
        try {
            supabaseService.deleteDare(dare.dareId) // Delete remotely
        } catch (e: Exception) {
            Log.e("DareRepository", "Failed to delete dare: ${dare.dareId}", e)
        }
    }
}
