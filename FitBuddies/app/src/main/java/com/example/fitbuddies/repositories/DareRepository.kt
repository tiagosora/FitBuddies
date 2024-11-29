package com.example.fitbuddies.repositories

import com.example.fitbuddies.models.Dare
import com.example.fitbuddies.local.DareDao
import com.example.fitbuddies.remote.SupabaseService
import kotlinx.coroutines.flow.Flow
import android.util.Log

class DareRepository(
    private val dareDao: DareDao,
    private val supabaseService: SupabaseService
) {

    fun getDares(userId: String): Flow<List<Dare>> = dareDao.getDares(userId)

    suspend fun insertDare(dare: Dare) {
        dareDao.insertDare(dare) // Save locally first
        try {
            supabaseService.insertDare(dare) // Sync to Supabase
        } catch (e: Exception) {
            Log.e("DareRepository", "Failed to sync dare: ${dare.dareId}", e)
        }
    }

    suspend fun refreshDares() {
        try {
            val remoteDares = supabaseService.getAllDares()
            remoteDares.forEach { dareDao.insertDare(it) } // Update local cache
        } catch (e: Exception) {
            Log.e("DareRepository", "Failed to refresh dares", e)
        }
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
