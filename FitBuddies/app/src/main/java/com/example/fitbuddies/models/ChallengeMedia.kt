package com.example.fitbuddies.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "challenge_media",
    foreignKeys = [
        ForeignKey(entity = Challenge::class, parentColumns = ["challengeId"], childColumns = ["challengeId"])
    ]
)
data class ChallengeMedia(
    @PrimaryKey val mediaId: String = UUID.randomUUID().toString(),
    val challengeId: String,
    val mediaUrl: String,
    val mediaType: MediaType,
    val timestamp: Long = System.currentTimeMillis()
)


