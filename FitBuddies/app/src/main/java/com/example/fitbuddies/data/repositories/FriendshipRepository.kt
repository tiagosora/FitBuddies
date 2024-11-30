package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.models.Friendship
import com.example.fitbuddies.data.local.FriendshipDao
import com.example.fitbuddies.data.remote.SupabaseService
import kotlinx.coroutines.flow.Flow
import android.util.Log

class FriendshipRepository(
    private val friendshipDao: FriendshipDao,
    private val supabaseService: SupabaseService
) {

    fun getFriends(userId: String): Flow<List<Friendship>> = friendshipDao.getFriends(userId)

    suspend fun insertFriendship(friendship: Friendship) {
        friendshipDao.insertFriendship(friendship) // Save locally first
        try {
            supabaseService.insertFriendship(friendship) // Sync to Supabase
        } catch (e: Exception) {
            Log.e("FriendshipRepository", "Failed to sync friendship", e)
        }
    }

    suspend fun refreshFriendships() {
        try {
            val remoteFriendships = supabaseService.getAllFriendships()
            remoteFriendships.forEach { friendshipDao.insertFriendship(it) } // Update local cache
        } catch (e: Exception) {
            Log.e("FriendshipRepository", "Failed to refresh friendships", e)
        }
    }

    suspend fun deleteFriendship(friendship: Friendship) {
        friendshipDao.deleteFriendship(friendship) // Remove locally
        try {
            supabaseService.deleteFriendship(friendship) // Delete remotely
        } catch (e: Exception) {
            Log.e("FriendshipRepository", "Failed to delete friendship", e)
        }
    }
}
