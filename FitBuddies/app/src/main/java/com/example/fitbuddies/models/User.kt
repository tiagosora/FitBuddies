package com.example.fitbuddies.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String = UUID.randomUUID().toString(),
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val workoutsCompleted: Int = 0,
    val distanceTraveled: Double = 0.0,
    val caloriesBurned: Int = 0,
    val profilePictureUrl: String? = null
)
