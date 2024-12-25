package com.example.fitbuddies.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = "users")
data class User(
    @PrimaryKey @SerialName("userid") val userId: String = UUID.randomUUID().toString(),
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("firstname") val firstName: String,
    @SerialName("lastname") val lastName: String,
    @SerialName("challengescompleted") val challengesCompleted: Int = 0,
    @SerialName("distancetraveled") val distanceTraveled: Double = 0.0,
    @SerialName("caloriesburned") val caloriesBurned: Int = 0,
    @SerialName("profilepictureurl") val profilePictureUrl: String? = null
)
