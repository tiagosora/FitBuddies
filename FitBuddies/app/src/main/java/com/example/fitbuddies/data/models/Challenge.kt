package com.example.fitbuddies.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(
    tableName = "challenges",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["daredById"])
    ]
)
data class Challenge(
    @PrimaryKey @SerialName("challengeid") val challengeId: String = UUID.randomUUID().toString(),
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("type") val type: String,
    @SerialName("daredbyid") val daredById: String,
    @SerialName("creationdate") val creationDate: String = System.currentTimeMillis().toString(),
    @SerialName("deadlinedate") val deadlineDate: String? = null,
    @SerialName("goal") val goal: Int = 0,
    @SerialName("pictureurl") var pictureUrl: String? = null
)
