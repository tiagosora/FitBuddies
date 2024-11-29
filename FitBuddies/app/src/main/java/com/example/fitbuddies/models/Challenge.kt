package com.example.fitbuddies.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "challenges",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["daredById"]),
    ]
)
data class Challenge(
    @PrimaryKey val challengeId: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val daredById: String,
    val creationDate: Long = System.currentTimeMillis(),
    val completionDate: Long? = null,
    val completionRate: Int = 0,
    val isCompleted: Boolean = false
)
