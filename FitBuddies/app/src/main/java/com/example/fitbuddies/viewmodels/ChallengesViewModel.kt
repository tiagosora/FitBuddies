package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChallengesViewModel : ViewModel() {
    private val _pendingChallenges = MutableStateFlow(
        listOf(

            Challenge("5K Run", "Complete a 5K run this week", "Running"),
            Challenge("10K Run", "Complete a 10K run this week", "Running"),
            Challenge("15K Run", "Complete a 15K run this week", "Running"),
        )
    )
    val pendingChallenges: StateFlow<List<Challenge>> = _pendingChallenges.asStateFlow()

    private val _acceptedChallenges = MutableStateFlow(
        listOf(
            Challenge("10K Steps", "Walk 10,000 steps daily for a week", "Walking"),
            Challenge("Plank Challenge", "Hold a plank for 2 minutes", "Weightlifting"),
            Challenge("20K Cycling", "Cycle 20 kilometers in one session", "Cycling"),
            Challenge("30K Cycling", "Cycle 30 kilometers in one session", "Cycling"),
            Challenge("40K Cycling", "Cycle 40 kilometers in one session", "Cycling"),
            Challenge("50K Cycling", "Cycle 50 kilometers in one session", "Cycling"),
            Challenge("60K Cycling", "Cycle 60 kilometers in one session", "Cycling"),
            Challenge("70K Cycling", "Cycle 70 kilometers in one session", "Cycling"),
        )
    )
    val acceptedChallenges: StateFlow<List<Challenge>> = _acceptedChallenges.asStateFlow()

    fun reorderAcceptedChallenges(fromIndex: Int, toIndex: Int) {
        val list = _acceptedChallenges.value.toMutableList()
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
        _acceptedChallenges.value = list
    }

    data class Challenge(
        val title: String,
        val description: String,
        val type: String
    )
}

