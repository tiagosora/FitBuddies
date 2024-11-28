package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _challenges = MutableStateFlow<List<Challenge>>(emptyList())
    val challenges: StateFlow<List<Challenge>> = _challenges

    private val _friendActivities = MutableStateFlow<List<FriendActivity>>(emptyList())
    val friendActivities: StateFlow<List<FriendActivity>> = _friendActivities

    init {
        // TODO: Replace with actual data fetching
        _challenges.value = listOf(
            Challenge("30 Day Plank", "Exercise", "Do a plank every day for 30 days"),
            Challenge("5K Run", "Running", "Train and complete a 5K run"),
            Challenge("100 Push-ups", "Exercise", "Do 100 push-ups in one day")
        )

        _friendActivities.value = listOf(
            FriendActivity("Jane Doe", "Cycling", "Cycling with Jane"),
            FriendActivity("John Smith", "Running", "Running with John"),
            FriendActivity("Alice Johnson", "Yoga", "Yoga session with Alice"),
            FriendActivity("Bob Brown", "Weightlifting", "Weightlifting with Bob"),
            FriendActivity("Jane Doe", "Cycling", "Cycling with Jane"),
            FriendActivity("John Smith", "Running", "Running with John"),
            FriendActivity("Alice Johnson", "Yoga", "Yoga session with Alice"),
            FriendActivity("Bob Brown", "Weightlifting", "Weightlifting with Bob"),
            FriendActivity("Jane Doe", "Cycling", "Cycling with Jane"),
            FriendActivity("John Smith", "Running", "Running with John"),
            FriendActivity("Alice Johnson", "Yoga", "Yoga session with Alice"),
            FriendActivity("Bob Brown", "Weightlifting", "Weightlifting with Bob"),
        )
    }
}

data class Challenge(val title: String, val type: String, val description: String, val progress: Float = 0.0f)
data class FriendActivity(val friendName: String, val activityType: String, val activityDescription: String)