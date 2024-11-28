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
            Friend("3", "Mike Johnson", "Last seen 1 day ago"),
            Friend("4", "Alice Brown", "Online"),
            Friend("5", "Bob White", "Last seen 3 days ago"),
            Friend("6", "Emily Green", "Last seen 1 week ago"),
            Friend("7", "Chris Black", "Online"),
            Friend("8", "Sarah Blue", "Last seen 2 weeks ago"),
            Friend("9", "Tom Grey", "Last seen 1 month ago"),
            Friend("10", "Laura Orange", "Online"),
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

    fun challengeFriend(id: String) {

    }

}

data class Friend(
    val id: String,
    val name: String,
    val status: String, // e.g., "Online", "Last seen 2 hours ago"
    val preferredWorkout: String = "Running",
    val lastWorkout: String = "2023-01-01"
)

data class FriendRequest(
    val id: String,
    val name: String,
)
