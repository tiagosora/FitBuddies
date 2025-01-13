package com.example.fitbuddies.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbuddies.data.repositories.FriendshipRepository
import com.example.fitbuddies.data.repositories.FriendshipRepository.RequestedFriendshipResponse
import com.example.fitbuddies.data.repositories.FriendshipRepository.FitBuddyCountChallenges
//import com.example.fitbuddies.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FitBuddiesViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
//    private val userRepository: UserRepository,
    private val friendshipRepository: FriendshipRepository
): ViewModel() {

    private val _friendShipRequests = MutableStateFlow<List<FriendshipRequest>>(emptyList())
    val friendShipRequests: StateFlow<List<FriendshipRequest>> = _friendShipRequests.asStateFlow()

    private val _fitBuddies = MutableStateFlow<List<FitBuddy>>(emptyList())
    val fitBuddies: StateFlow<List<FitBuddy>> = _fitBuddies.asStateFlow()

    init {
        fetchFriendshipRequests()
        fetchFitBuddies()
    }

    private fun fetchFriendshipRequests() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("currentUserId", null)
            if (userId != null) {
                val friendshipRequestsData: List<RequestedFriendshipResponse> = friendshipRepository.getUserFriendshipRequests(userId)
                _friendShipRequests.value = friendshipRequestsData.map {
                    FriendshipRequest(it.users.userid, "${it.users.firstname} ${it.users.lastname}", it.users.profilepictureurl)
                }
            }
        }
    }

    private fun fetchFitBuddies() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("currentUserId", null)
            if (userId != null) {
                val fitBuddiesDetailsWithChallenges: List<FitBuddyCountChallenges> = friendshipRepository.getFitBuddiesDetailsWithCountChallenges(userId)
                _fitBuddies.value = fitBuddiesDetailsWithChallenges.map {
                    FitBuddy(it.fitbuddyid, "${it.firstname} ${it.lastname}", it.challengescompletedcount, it.profilepictureurl)
                }
            }
            println(_fitBuddies.value)
            _fitBuddies.value = _fitBuddies.value.sortedByDescending { it.numberOfCompletedChallenges }
        }
    }

    data class FitBuddy(val id: String, val name: String, val numberOfCompletedChallenges: Int = 0, val profilePictureUrl: String = "")
    data class FriendshipRequest(val id: String, val name: String, val profilePictureUrl: String = "")
}

