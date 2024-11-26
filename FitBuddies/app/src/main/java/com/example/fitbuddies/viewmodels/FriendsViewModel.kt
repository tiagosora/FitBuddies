package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FriendsViewModel: ViewModel() {
    private val _friends = MutableStateFlow<List<Friend>>(emptyList())
    val friends: StateFlow<List<Friend>> = _friends

    private val _friendRequests = MutableStateFlow<List<FriendRequest>>(emptyList())
    val friendRequests: StateFlow<List<FriendRequest>> = _friendRequests

    init {
        _friends.value = listOf(
            Friend("1", "John Doe", "Online"),
            Friend("2", "Jane Smith", "Last seen 2 hours ago"),
            Friend("3", "Mike Johnson", "Last seen 1 day ago")
        )
        _friendRequests.value = listOf(
            FriendRequest("4", "Alice Brown"),
            FriendRequest("5", "Bob White")
        )
    }

    fun acceptFriendRequest(requestId: String) {
        // Logic to accept the friend request
    }

    fun denyFriendRequest(requestId: String) {
        // Logic to deny the friend request
    }

}

data class Friend(
    val id: String,
    val name: String,
    val status: String // e.g., "Online", "Last seen 2 hours ago"
)

data class FriendRequest(
    val id: String,
    val name: String,
)
