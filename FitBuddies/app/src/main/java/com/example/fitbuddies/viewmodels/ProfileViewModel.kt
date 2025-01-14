package com.example.fitbuddies.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbuddies.data.models.User
import com.example.fitbuddies.data.repositories.BucketRepository
import com.example.fitbuddies.data.repositories.ChallengeRepository
import com.example.fitbuddies.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _userCompletedChallenges = MutableStateFlow<List<ChallengeInfo>>(emptyList())
    val userCompletedChallenges: StateFlow<List<ChallengeInfo>> = _userCompletedChallenges.asStateFlow()

    init {
        fetchUser()
        fetchUserCompletedChallenges()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("currentUserId", null)
            if (userId != null) {
                val userData = userRepository.getUserById(userId)
                println("ProfileViewModel: $userData")
                try {
                    userData?.profilePictureUrl = BucketRepository.getImageURL(userData.profilePictureUrl.toString())
                } catch (e: Exception) {
                    println("ProfileViewModel: $e")
                }
                println("ProfileViewModel: $userData")
                _user.value = userData
            }
        }
    }

    private fun fetchUserCompletedChallenges() {
        viewModelScope.launch {
            val userId = sharedPreferences.getString("currentUserId", null)
            if (userId != null) {
                val userCompletedChallengesData = challengeRepository.getUserCompletedChallenges(userId)
                _userCompletedChallenges.value = userCompletedChallengesData.map {
                    ChallengeInfo(it.challenges.title, it.challenges.type, it.completiondate.toString().substring(0..9), it.challenges.description)
                }
            }
        }
    }

    val baseChallengesGoal = 5
    val challengeGrowthRate = 1.5
    val baseDistanceGoal = 10
    val distanceGrowthRate = 1.3
    val baseCaloriesGoal = 1000
    val calorieGrowthRate = 1.4

    val numberOfLevels = 20
    val challengesGoals = (1..numberOfLevels).map { level -> (baseChallengesGoal * challengeGrowthRate.pow((level - 1).toDouble())).toInt() }
    val distanceGoals = (1..numberOfLevels).map { level -> (baseDistanceGoal * distanceGrowthRate.pow((level - 1).toDouble())).toInt() }
    val caloriesGoals = (1..numberOfLevels).map { level -> (baseCaloriesGoal * calorieGrowthRate.pow((level - 1).toDouble())).toInt() }

    data class ChallengeInfo(val title: String, val type: String, val date: String, val description: String)
}

