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

    // Get all friendships for a user
    @Query("SELECT * FROM friendships WHERE userId = :userId")
    fun getFriendshipsForUser(userId: String): Flow<List<Friendship>>

    // Accept a friend request
    @Query("UPDATE friendships SET isAccepted = 1 WHERE userId = :userId AND friendId = :friendId")
    suspend fun acceptFriendRequest(userId: String, friendId: String)

    // Create a friend request
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriendship(friendship: Friendship)

    // Decline a friend request
    @Delete
    suspend fun deleteFriendship(friendship: Friendship)
}
