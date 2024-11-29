package com.example.fitbuddies.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.fitbuddies.models.Friendship

@Dao
interface FriendshipDao {
    @Query("SELECT * FROM friendships WHERE userId = :userId")
    fun getFriends(userId: String): Flow<List<Friendship>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriendship(friendship: Friendship)

    @Delete
    suspend fun deleteFriendship(friendship: Friendship)
}
