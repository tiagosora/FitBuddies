package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {
    private val _profile = MutableStateFlow(
        Profile(
            name = "John Doe",
            email = "john.doe@example.com",
            profilePicUrl = "",
            totalWorkouts = 42,
            totalDistance = 1.5722223E17f,
            totalCaloriesBurned = 12222500,
            weeklyWorkoutGoal = 5,
            weeklyDistanceGoal = 30f,
            weeklyCalorieGoal = 2000
        )
    )
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    private val _recentActivities = MutableStateFlow(
        listOf(
            Activity("Morning Run", "Running", "Today", "30 min"),
            Activity("Strength Training", "Weightlifting", "Yesterday", "45 min"),
            Activity("Evening Cycle", "Cycling", "2 days ago", "1 hr")
        )
    )
    val recentActivities: StateFlow<List<Activity>> = _recentActivities.asStateFlow()

    data class Profile(
        val name: String,
        val email: String,
        val profilePicUrl: String,
        val totalWorkouts: Int,
        val totalDistance: Float,
        val totalCaloriesBurned: Int,
        val weeklyWorkoutGoal: Int,
        val weeklyDistanceGoal: Float,
        val weeklyCalorieGoal: Int
    )

    data class Activity(
        val name: String,
        val type: String,
        val date: String,
        val duration: String
    )
}

