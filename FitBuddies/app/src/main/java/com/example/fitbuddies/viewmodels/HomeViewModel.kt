package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    val dailySteps: Int = 8234

    private val _activeChallenges = MutableStateFlow(
        listOf(
            ActiveChallenge("30 Day Plank", "Exercise", "Do a plank every day for 30 days"),
            ActiveChallenge("5K Run", "Running", "Train and complete a 5K run"),
            ActiveChallenge("100 Push-ups", "Exercise", "Do 100 push-ups in one day")
        )
    )
    val activeChallenges: StateFlow<List<ActiveChallenge>> = _activeChallenges

    private val _fitBuddiesChallenges = MutableStateFlow(
        listOf(
            FitBuddyChallenge("Jane Doe", "Cycling", "Cycling with Jane"),
            FitBuddyChallenge("John Smith", "Running", "Running with John"),
            FitBuddyChallenge("Alice Johnson", "Yoga", "Yoga session with Alice"),
            FitBuddyChallenge("Bob Brown", "Weightlifting", "Weightlifting with Bob"),
            FitBuddyChallenge("Jane Doe", "Cycling", "Cycling with Jane"),
            FitBuddyChallenge("John Smith", "Running", "Running with John"),
            FitBuddyChallenge("Alice Johnson", "Yoga", "Yoga session with Alice"),
            FitBuddyChallenge("Bob Brown", "Weightlifting", "Weightlifting with Bob"),
            FitBuddyChallenge("Jane Doe", "Cycling", "Cycling with Jane"),
            FitBuddyChallenge("John Smith", "Running", "Running with John"),
            FitBuddyChallenge("Alice Johnson", "Yoga", "Yoga session with Alice"),
            FitBuddyChallenge("Bob Brown", "Weightlifting", "Weightlifting with Bob"),
        )
    )
    val fitBuddiesChallenges: StateFlow<List<FitBuddyChallenge>> = _fitBuddiesChallenges

    data class ActiveChallenge(val title: String, val type: String, val description: String, val completionRate: Float = 0.0f)
    data class FitBuddyChallenge(val fitBuddyName: String, val lastChallengeType: String, val lastChallengeDescription: String)
}
