package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.models.Friendship
import com.example.fitbuddies.data.local.FriendshipDao
import android.util.Log

class FriendshipRepository(
    private val friendshipDao: FriendshipDao,
) {

    suspend fun insertFriendship(friendship: Friendship) {
        friendshipDao.insertFriendship(friendship) // Save locally first
        try {
        } catch (e: Exception) {
            Log.e("FriendshipRepository", "Failed to sync friendship", e)
        }
    }

    suspend fun acceptFriendRequest(userId: String, friendId: String) {
        friendshipDao.acceptFriendRequest(userId, friendId) // Update locally
        try {
        } catch (e: Exception) {
            Log.e("FriendshipRepository", "Failed to accept friend request", e)
        }
    }

    suspend fun deleteFriendship(friendship: Friendship) {
        friendshipDao.deleteFriendship(friendship) // Remove locally
        try {
        } catch (e: Exception) {
            Log.e("FriendshipRepository", "Failed to delete friendship", e)
        }
    }
}
