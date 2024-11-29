package com.example.fitbuddies.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "dares",
    foreignKeys = [
        ForeignKey(entity = Challenge::class, parentColumns = ["challengeId"], childColumns = ["challengeId"]),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["daredToId"])
    ]
)
data class Dare(
    @PrimaryKey val dareId: String = UUID.randomUUID().toString(),
    val challengeId: String,
    val daredToId: String,
    val isAccepted: Boolean = false
)