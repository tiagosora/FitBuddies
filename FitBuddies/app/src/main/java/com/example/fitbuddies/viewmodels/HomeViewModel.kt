package com.example.fitbuddies.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbuddies.data.models.User
import com.example.fitbuddies.data.repositories.ChallengeRepository
import com.example.fitbuddies.data.repositories.ChallengeRepository.DareToChallengeResponse
import com.example.fitbuddies.data.repositories.ChallengeRepository.FitBuddyLastCompletedChallenge
import com.example.fitbuddies.data.repositories.FriendshipRepository
import com.example.fitbuddies.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository,
    private val friendshipRepository: FriendshipRepository,
    private val challengeRepository: ChallengeRepository
): ViewModel() {

    val dailySteps: Int = 8234

    private val _activeChallenges = MutableStateFlow<List<ActiveChallenge>>(emptyList())
    val activeChallenges: StateFlow<List<ActiveChallenge>> = _activeChallenges.asStateFlow()

    private val _fitBuddiesChallenges = MutableStateFlow<List<FitBuddyChallenge>>(emptyList())
    val fitBuddiesChallenges: StateFlow<List<FitBuddyChallenge>> = _fitBuddiesChallenges.asStateFlow()

    init {
        fetchActiveChallenges()
        fetchFitBuddiesChallenges()
    }

    private fun fetchActiveChallenges() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("currentUserId", null)
            if (userId != null) {
                val activeChallengesData : List<DareToChallengeResponse> = challengeRepository.getActiveUserChallenges(userId)
                _activeChallenges.value = activeChallengesData.map {
                    ActiveChallenge(it.challenges.challengeid, it.challenges.title, it.challenges.type, it.challenges.description)
                }
            }
//            println("Active Challenges: ${_activeChallenges.value}")
        }
    }

    private fun fetchFitBuddiesChallenges() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("currentUserId", null)
            if (userId != null) {
                val userFitBuddiesIds: List<String> = friendshipRepository.getUserAcceptedFriendships(userId).map {
                    if (it.userId == userId) { it.friendId }
                    else { it.userId }
                }
                val fitBuddies: List<User> = userRepository.getUsersByIds(userFitBuddiesIds)
//                println("User Fit Buddies: $userFitBuddies")
                val fitBuddiesChallengesData: List<FitBuddyLastCompletedChallenge> = challengeRepository.getFitBuddiesChallenges(userFitBuddiesIds)
                _fitBuddiesChallenges.value = fitBuddiesChallengesData.map {
                    val fitBuddy = fitBuddies.find { fitBuddy -> fitBuddy.userId == it.daredtoid }!!
                    FitBuddyChallenge(it.challengeid, it.daredtoid, "${fitBuddy.firstName} ${fitBuddy.lastName}", it.type, it.title)
                }
//                println("Fit Buddies Challenges: $fitBuddiesChallengesData")
            }
        }
    }

    data class ActiveChallenge(val challengeId: String, val title: String, val type: String, val description: String, val completionRate: Float = 0.0f)
    data class FitBuddyChallenge(val challengeId: String, val fitBuddyId: String,val fitBuddyName: String, val lastChallengeType: String, val lastChallengeTitle: String)
}
