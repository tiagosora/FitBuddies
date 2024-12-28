package com.example.fitbuddies.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(
    tableName = "challenge_media",
    foreignKeys = [
        ForeignKey(entity = Challenge::class, parentColumns = ["challengeId"], childColumns = ["challengeId"])
    ]
)
data class ChallengeMedia(
    @PrimaryKey @SerialName("mediaid") val mediaId: String = UUID.randomUUID().toString(),
    @SerialName("challengeid") val challengeId: String,
    @SerialName("mediaurl") val mediaUrl: String,
    @SerialName("mediatype") val mediaType: MediaType,
    @SerialName("timestamp") val timestamp: String = System.currentTimeMillis().toString()
)


