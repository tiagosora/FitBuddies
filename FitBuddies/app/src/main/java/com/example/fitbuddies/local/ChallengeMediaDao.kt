package com.example.fitbuddies.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.fitbuddies.models.ChallengeMedia

@Dao
interface ChallengeMediaDao {
    @Query("SELECT * FROM challenge_media WHERE dareId = :dareId")
    fun getMediaByDare(dareId: String): Flow<List<ChallengeMedia>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: ChallengeMedia)

    @Delete
    suspend fun deleteMedia(media: ChallengeMedia)
}
