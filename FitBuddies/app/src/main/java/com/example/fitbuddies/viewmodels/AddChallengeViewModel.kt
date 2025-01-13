package com.example.fitbuddies.viewmodels

import android.content.SharedPreferences
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbuddies.data.models.Challenge
import com.example.fitbuddies.data.repositories.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
//import com.example.fitbuddies.data.models.Friendship
import com.example.fitbuddies.data.models.User
import com.example.fitbuddies.data.repositories.FriendshipRepository
import com.example.fitbuddies.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

@HiltViewModel
class AddChallengeViewModel @Inject constructor(
    private val friendshipRepository: FriendshipRepository,
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences,
    private val challengeRepository: ChallengeRepository = ChallengeRepository()
) : ViewModel() {

//    private val _friendsList = MutableStateFlow<List<Friendship>>(emptyList())
//    val friendsList: StateFlow<List<Friendship>> = _friendsList.asStateFlow()

    fun createChallenge(title: String, description: String, type: String, duration: Int, goal: Int, photoBitmap: ImageBitmap?): Challenge? {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("currentUserId", null)
            // current date + duration
            if (userId != null) {
                val newChallenge = challengeRepository.createChallenge(userId, title, description, type, System.currentTimeMillis() + duration, goal, photoBitmap)
                println("New challenge created: $newChallenge")
            }
            println("Failed to get current user id")
        }
        return null
    }

    suspend fun fetchFriends(): List<User> {
        return withContext(Dispatchers.IO) {
            val userId = sharedPreferences.getString("currentUserId", null)
            try {
                val friendships = friendshipRepository.getUserAcceptedFriendships(userId.toString())
                val friendIds = friendships.map {
                    if (it.userId == userId) it.friendId else it.userId
                }
                userRepository.getUsersByIds(friendIds)
            } catch (e: Exception) {
                println("Error fetching friends: ${e.message}")
                emptyList()
            }
        }
    }
}
