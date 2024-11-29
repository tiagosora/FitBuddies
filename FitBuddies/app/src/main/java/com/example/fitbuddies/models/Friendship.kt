package com.example.fitbuddies.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "friendships",
    primaryKeys = ["userId", "friendId"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["userId"]),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["friendId"])
    ]
)
data class Friendship(
    val userId: String,
    val friendId: String,
    val isAccepted: Boolean = false
)