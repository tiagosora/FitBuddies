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
            Challenge("30 Day Plank", "Do a plank every day for 30 days"),
            Challenge("5K Run", "Train and complete a 5K run"),
            Challenge("100 Push-ups", "Do 100 push-ups in one day")
        )

        _friendActivities.value = listOf(
            FriendActivity("John Doe", "Completed the 30 Day Plank challenge"),
            FriendActivity("Jane Smith", "Started a new 5K Run challenge"),
            FriendActivity("Mike Johnson", "Invited you to the 100 Push-ups challenge"),
            FriendActivity("John Doe", "Completed the 30 Day Plank challenge"),
            FriendActivity("Jane Smith", "Started a new 5K Run challenge"),
            FriendActivity("Mike Johnson", "Invited you to the 100 Push-ups challenge"),
            FriendActivity("John Doe", "Completed the 30 Day Plank challenge"),
            FriendActivity("Jane Smith", "Started a new 5K Run challenge"),
            FriendActivity("Mike Johnson", "Invited you to the 100 Push-ups challenge"),
            FriendActivity("John Doe", "Completed the 30 Day Plank challenge"),
            FriendActivity("Jane Smith", "Started a new 5K Run challenge"),
            FriendActivity("Mike Johnson", "Invited you to the 100 Push-ups challenge"),
            FriendActivity("John Doe", "Completed the 30 Day Plank challenge"),
            FriendActivity("Jane Smith", "Started a new 5K Run challenge"),
            FriendActivity("Mike Johnson", "Invited you to the 100 Push-ups challenge"),
        )
    }
}

data class Challenge(val title: String, val description: String)
data class FriendActivity(val friendName: String, val activityDescription: String)