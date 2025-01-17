package com.example.fitbuddies.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.fitbuddies.data.models.ChallengeMedia

@Dao
interface ChallengeMediaDao {
    @Query("SELECT * FROM challenge_media WHERE challengeId = :dareId")
    fun getMediaByChallenge(dareId: String): Flow<List<ChallengeMedia>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: ChallengeMedia)

    @Delete
    suspend fun deleteMedia(media: ChallengeMedia)
}
