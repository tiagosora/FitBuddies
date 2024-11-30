package com.example.fitbuddies.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.fitbuddies.data.models.Challenge

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges WHERE userId = :userId")
    fun getChallengesForUser(userId: String): Flow<List<Challenge>>

    @Query("SELECT * FROM challenges WHERE challengeId = :challengeId")
    suspend fun getChallengeById(challengeId: String): Challenge?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenge(challenge: Challenge)

    @Delete
    suspend fun deleteChallenge(challenge: Challenge)
}
