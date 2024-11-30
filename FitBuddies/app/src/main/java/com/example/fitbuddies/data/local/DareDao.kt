package com.example.fitbuddies.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.fitbuddies.data.models.Dare

@Dao
interface DareDao {
    @Query("SELECT * FROM dares WHERE daredToId = :userId OR daredById = :userId")
    fun getDares(userId: String): Flow<List<Dare>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDare(dare: Dare)

    @Delete
    suspend fun deleteDare(dare: Dare)
}
