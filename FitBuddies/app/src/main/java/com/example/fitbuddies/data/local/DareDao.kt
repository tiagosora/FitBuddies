package com.example.fitbuddies.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fitbuddies.data.models.Dare

@Dao
interface DareDao {

    // Create a dare
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDare(dare: Dare)

    // Accept a dare by challengeId and userId
    @Query("UPDATE dares SET isAccepted = 1 WHERE challengeId = :challengeId AND daredToId = :userId")
    suspend fun acceptDare(challengeId: String, userId: String)

    // Decline the dare
    @Delete
    suspend fun deleteDare(dare: Dare)
}
