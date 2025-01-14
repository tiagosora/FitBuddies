package com.example.fitbuddies.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigInteger

@Serializable
@Entity(
    tableName = "friendships",
    primaryKeys = ["userId", "friendId"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"]),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["friendId"])
    ]
)
data class Friendship(
    @SerialName("userid") val userId: String,
    @SerialName("friendid") val friendId: String,
    @SerialName("isaccepted") val isAccepted: Boolean = false,
    @SerialName("creationdate") val creationDate: Long = System.currentTimeMillis().toLong()
)