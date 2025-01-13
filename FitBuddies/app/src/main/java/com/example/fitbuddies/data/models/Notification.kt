package com.example.fitbuddies.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(
    tableName = "notifications",
    foreignKeys = [
        ForeignKey(entity = Challenge::class, parentColumns = ["challengeId"], childColumns = ["daredById"])
    ]
)

data class Notification(
    @PrimaryKey @SerialName("notificationid") val notificationId: String = UUID.randomUUID().toString(),
    @SerialName("challengeid") val challengeId: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("status") val status: String
)
