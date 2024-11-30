package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FitBuddiesViewModel : ViewModel() {
    private val _fitBuddies = MutableStateFlow<List<FitBuddy>>(
        listOf(
            FitBuddy("John Doe", "Online"),
            FitBuddy("Jane Smith", "Last seen 2 hours ago"),
            FitBuddy("Mike Johnson", "Last seen 1 day ago"),
            FitBuddy("Alice Brown", "Online"),
            FitBuddy("Bob White", "Last seen 3 days ago"),
            FitBuddy("Emily Green", "Last seen 1 week ago"),
            FitBuddy("Chris Black", "Online"),
            FitBuddy("Sarah Blue", "Last seen 2 weeks ago"),
            FitBuddy("Tom Grey", "Last seen 1 month ago"),
            FitBuddy("Laura Orange", "Online"),
        )
    )
    val fitBuddies: StateFlow<List<FitBuddy>> = _fitBuddies

    private val _friendShipRequests = MutableStateFlow<List<FriendshipRequest>>(
        listOf(
            FriendshipRequest("Alice Brown"),
            FriendshipRequest("Bob White")
        )
    )
    val friendShipRequests: StateFlow<List<FriendshipRequest>> = _friendShipRequests
}

data class FitBuddy(
    val name: String,
    val status: String, // e.g., "Online", "Last seen 2 hours ago"
    val profilePictureUrl: String = "",
)

data class FriendshipRequest(
    val name: String,
)
