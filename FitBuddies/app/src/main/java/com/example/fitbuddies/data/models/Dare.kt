package com.example.fitbuddies.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(
    tableName = "dares",
    foreignKeys = [
        ForeignKey(entity = Challenge::class, parentColumns = ["challengeId"], childColumns = ["challengeId"]),
        ForeignKey(entity = User::class, parentColumns = ["userId"], childColumns = ["daredToId"])
    ]
)
data class Dare(
    @PrimaryKey @SerialName("dareid") val dareId: String = UUID.randomUUID().toString(),
    @SerialName("challengeid") val challengeId: String,
    @SerialName("daredtoid") val daredToId: String,
    @SerialName("isaccepted") val isAccepted: Boolean = false,
    @SerialName("iscompleted") val isCompleted: Boolean = false,
    @SerialName("completiondate") val completionDate: Long? = null,
    @SerialName("completionrate") val completionRate: Int = 0
)