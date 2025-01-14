package com.example.fitbuddies.viewmodels

import android.Manifest
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
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val userRepository: UserRepository,
    private val friendshipRepository: FriendshipRepository,
    private val challengeRepository: ChallengeRepository
): ViewModel(), SensorEventListener {

    private val _hasStepPermission = MutableStateFlow(false)
    val hasStepPermission: StateFlow<Boolean> = _hasStepPermission.asStateFlow()

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _dailySteps = MutableStateFlow(0)
    val dailySteps: StateFlow<Int> = _dailySteps.asStateFlow()

    private var initialSteps: Int = -1

    fun checkPermissionAndRegisterSensor() {
        _hasStepPermission.value = hasRequiredPermissions()
        if (_hasStepPermission.value) {
            registerStepSensor()
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun registerStepSensor() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            // Tratar caso onde o sensor não está disponível
            println("Step sensor not available on this device")
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()

            if (initialSteps == -1) {
                // Primeira leitura do dia
                initialSteps = totalSteps
            }

            // Calcular passos do dia
            val dailySteps = totalSteps - initialSteps
            _dailySteps.value = dailySteps

            // Salvar o valor para persistência
            sharedPreferences.edit()
                .putInt("daily_steps", dailySteps)
                .putInt("initial_steps", initialSteps)
                .apply()
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Não é necessário tratar isto para passos
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }



    private val _activeChallenges = MutableStateFlow<List<ActiveChallenge>>(emptyList())
    val activeChallenges: StateFlow<List<ActiveChallenge>> = _activeChallenges.asStateFlow()

    private val _fitBuddiesChallenges = MutableStateFlow<List<FitBuddyChallenge>>(emptyList())
    val fitBuddiesChallenges: StateFlow<List<FitBuddyChallenge>> = _fitBuddiesChallenges.asStateFlow()

    init {

        initialSteps = sharedPreferences.getInt("initial_steps", -1)
        _dailySteps.value = sharedPreferences.getInt("daily_steps", 0)

        checkPermissionAndRegisterSensor()
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
