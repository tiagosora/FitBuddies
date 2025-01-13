package com.example.fitbuddies.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.fitbuddies.data.models.Friendship

@Dao
interface FriendshipDao {

    // Get all friendships for a user (accepted only)
    @Query("SELECT * FROM friendships WHERE (userId = :userId OR friendId = :userId) AND isAccepted = 1")
    fun getAcceptedFriendships(userId: String): Flow<List<Friendship>>

    // Get pending friendship requests for a user
    @Query("SELECT * FROM friendships WHERE friendId = :userId AND isAccepted = 0")
    fun getPendingFriendshipRequests(userId: String): Flow<List<Friendship>>

    // Accept a friend request
    @Query("UPDATE friendships SET isAccepted = 1 WHERE userId = :userId AND friendId = :friendId")
    suspend fun acceptFriendRequest(userId: String, friendId: String)

    // Create a friend request
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriendship(friendship: Friendship)

    // Decline a friend request
    @Query("DELETE FROM friendships WHERE userId = :userId AND friendId = :friendId")
    suspend fun declineFriendRequest(userId: String, friendId: String)

    // Remove a friendship (unfriend)
    @Query("DELETE FROM friendships WHERE (userId = :userId AND friendId = :friendId) OR (userId = :friendId AND friendId = :userId)")
    suspend fun removeFriendship(userId: String, friendId: String)
}
