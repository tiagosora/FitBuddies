package com.example.fitbuddies.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbuddies.data.repositories.ChallengeRepository
import com.example.fitbuddies.data.repositories.ChallengeRepository.DareToChallengeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengesViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val challengeRepository: ChallengeRepository
): ViewModel()
{
    private val _pendingChallenges = MutableStateFlow<List<ChallengeInfo>>(emptyList())
    val pendingChallenges: StateFlow<List<ChallengeInfo>> = _pendingChallenges.asStateFlow()

    private val _acceptedChallenges = MutableStateFlow<List<ChallengeInfo>>(emptyList())
    val acceptedChallenges: StateFlow<List<ChallengeInfo>> = _acceptedChallenges.asStateFlow()

    init {
        fetchPendingChallenges()
        fetchAcceptedChallenges()
    }

    private fun fetchPendingChallenges() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("currentUserId", null)
            if (userId != null) {
                val pendingChallengesData: List<DareToChallengeResponse> = challengeRepository.getRequestedUserChallenges(userId)
                _pendingChallenges.value = pendingChallengesData.map {
                    ChallengeInfo(it.challenges.challengeid, it.challenges.title, it.challenges.description, it.challenges.type)
                }
            }
//            println("Pending Challenges: ${_pendingChallenges.value}")
        }
    }

    private fun fetchAcceptedChallenges() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("currentUserId", null)
            if (userId != null) {
                val acceptedChallengesData : List<DareToChallengeResponse> = challengeRepository.getActiveUserChallenges(userId)
                _acceptedChallenges.value = acceptedChallengesData.map {
                    ChallengeInfo(it.challenges.challengeid, it.challenges.title, it.challenges.description, it.challenges.type)
                }
            }
//            println("Accepted Challenges: ${_activeChallenges.value}")
        }
    }


    fun reorderAcceptedChallenges(fromIndex: Int, toIndex: Int) {
        val list = _acceptedChallenges.value.toMutableList()
        val item = list.removeAt(fromIndex)
        list.add(toIndex, item)
        _acceptedChallenges.value = list
    }

    data class ChallengeInfo(val id: String, val title: String, val description: String, val type: String)
}

