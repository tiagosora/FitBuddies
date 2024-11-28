package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChallengesViewModel : ViewModel() {
    private val _pendingChallenges = MutableStateFlow(
        listOf(
            Challenge("1", "5K Run", "Complete a 5K run this week", "Running"),
            Challenge("2", "100 Push-ups", "Do 100 push-ups in a single day", "Weightlifting"),
            Challenge("3", "30-min Yoga", "Practice yoga for 30 minutes", "Other")
        )
    )
    val pendingChallenges: StateFlow<List<Challenge>> = _pendingChallenges.asStateFlow()

    private val _acceptedChallenges = MutableStateFlow(
        listOf(
            Challenge("4", "10K Steps", "Walk 10,000 steps daily for a week", "Walking"),
            Challenge("5", "Plank Challenge", "Hold a plank for 2 minutes", "Weightlifting"),
            Challenge("6", "20K Cycling", "Cycle 20 kilometers in one session", "Cycling"),
            Challenge("7", "1K Swim", "Swim 1 kilometer in a single session", "Swimming"),
            Challenge("8", "50 Squats", "Do 50 squats in a single day", "Weightlifting"),
            Challenge("9", "15-min Meditation", "Meditate for 15 minutes daily", "Other"),
            Challenge("10", "5K Rowing", "Row 5 kilometers in one session", "Rowing"),
        )
    )
    val acceptedChallenges: StateFlow<List<Challenge>> = _acceptedChallenges.asStateFlow()

    fun acceptChallenge(challengeId: String) {
        val challenge = _pendingChallenges.value.find { it.id == challengeId }
        challenge?.let {
            _pendingChallenges.value -= it
            _acceptedChallenges.value += it
        }
    }

    fun denyChallenge(challengeId: String) {
        _pendingChallenges.value = _pendingChallenges.value.filter { it.id != challengeId }
    }

    fun reorderAcceptedChallenges(fromIndex: Int, toIndex: Int) {
        val list = _acceptedChallenges.value.toMutableList()
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
        _acceptedChallenges.value = list
    }

    data class Challenge(
        val id: String,
        val title: String,
        val description: String,
        val type: String
    )
}

