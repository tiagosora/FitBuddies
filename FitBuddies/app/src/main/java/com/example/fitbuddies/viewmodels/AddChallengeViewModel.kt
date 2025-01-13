package com.example.fitbuddies.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbuddies.data.models.Friendship
import com.example.fitbuddies.data.models.User
import com.example.fitbuddies.data.repositories.FriendshipRepository
import com.example.fitbuddies.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddChallengeViewModel @Inject constructor(
    private val friendshipRepository: FriendshipRepository,
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _creationStatus = MutableStateFlow<CreationStatus>(CreationStatus.Idle)
    val creationStatus: StateFlow<CreationStatus> = _creationStatus.asStateFlow()

    private val _friendsList = MutableStateFlow<List<Friendship>>(emptyList())
    val friendsList: StateFlow<List<Friendship>> = _friendsList.asStateFlow()

    val userId: String
        get() = sharedPreferences.getString("currentUserId", "") ?: ""

    fun createChallenge(title: String, description: String, type: String, duration: Int, goal: Int) {
        viewModelScope.launch {
            _creationStatus.value = CreationStatus.Loading
            try {
                kotlinx.coroutines.delay(1000) // Simula uma chamada de API
                _creationStatus.value = CreationStatus.Success
            } catch (e: Exception) {
                _creationStatus.value = CreationStatus.Error(e.message ?: "Unknown Error")
            }
        }
    }

    suspend fun fetchFriends(): List<User> {
        return withContext(Dispatchers.IO) {
            try {
                val friendships = friendshipRepository.getUserAcceptedFriendships(userId)
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



    sealed class CreationStatus {
        object Idle : CreationStatus()
        object Loading : CreationStatus()
        object Success : CreationStatus()
        data class Error(val message: String) : CreationStatus()
    }
}
