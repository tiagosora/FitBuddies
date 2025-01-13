package com.example.fitbuddies.viewmodels

import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbuddies.data.models.Challenge
import com.example.fitbuddies.data.repositories.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddChallengeViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val challengeRepository: ChallengeRepository = ChallengeRepository()
) : ViewModel() {


    fun createChallenge(title: String, description: String, type: String, duration: Int, goal: Int, photoBitmap: Bitmap): Challenge? {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("currentUserId", null)
            if (userId != null) {
                val newChallenge = challengeRepository.createChallenge(userId, title, description, type, duration, goal, photoBitmap)
                println("New challenge created: $newChallenge")
            }
            println("Failed to get current user id")
        }
        return null
    }

}
