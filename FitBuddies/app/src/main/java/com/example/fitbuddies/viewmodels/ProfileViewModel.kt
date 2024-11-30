package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ProfileViewModel : ViewModel() {
    private val _user = MutableStateFlow(
        User(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            profilePictureUrl = "",
            challengesCompleted = 42,
            distanceTraveled = 35.00,
            caloriesBurned = 8627,
        )
    )
    val user: StateFlow<User> = _user.asStateFlow()

    private val _challenges = MutableStateFlow(
        listOf(
            Challenge(title = "30 Day Plank", type = "Exercise", date = "2021-09-01", description = "Do a plank every day for 30 days"),
            Challenge(title = "5K Run", type = "Running", date = "2021-09-01", description = "Train and complete a 5K run"),
            Challenge(title = "5K Bicycling", type = "Bicycling", date = "2021-09-01", description = "Train and complete a 5K bicycling"),
            Challenge(title = "100 Push-ups", type = "Exercise", date = "2021-09-01", description = "Do 100 push-ups in one day"),
            Challenge(title = "100 Squats", type = "Exercise", date = "2021-09-01", description = "Do 100 squats in one day"),
            Challenge(title = "100 Sit-ups", type = "Exercise", date = "2021-09-01", description = "Do 100 sit-ups in one day"),
        )
    )
    val challenges: StateFlow<List<Challenge>> = _challenges.asStateFlow()

    // List of numbers of goals to be completed
    val challengesGoals = listOf(5, 10, 15, 20, 25, 30, 35, 40, 45, 50)
    val distanceGoals = listOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
    val caloriesGoals = listOf(1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000)

    data class User(
        val email: String,
        val firstName: String,
        val lastName: String,
        val challengesCompleted: Int,
        val distanceTraveled: Double,
        val caloriesBurned: Int,
        val profilePictureUrl: String? = null
    )

    data class Challenge(
        val title: String,
        val type: String,
        val date: String,
        val description: String
    )
}

