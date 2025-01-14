package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbuddies.data.models.Dare
import com.example.fitbuddies.data.models.User
import com.example.fitbuddies.data.repositories.ChallengeRepository
import com.example.fitbuddies.data.repositories.DareRepository
import com.example.fitbuddies.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Participant(
    val id: String,
    val name: String,
    val avatarUrl: String?
)

data class ChallengeDetails(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val description: String,
    val type: String,
    val deadline: String,
    val goal: Int,
    val participants: List<Participant>
)

@HiltViewModel
class ChallengeDetailsViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val dareRepository: DareRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _challengeDetails = MutableStateFlow<ChallengeDetails?>(null)
    val challengeDetails: StateFlow<ChallengeDetails?> = _challengeDetails.asStateFlow()

    fun loadChallengeDetails(challengeId: String) {
        viewModelScope.launch {
            val challenge = challengeRepository.getChallengeById(challengeId)
            var associatedDares : List<Dare> = dareRepository.getDaredByChallengeId(challengeId)
            var participantsIds : List<String> = associatedDares.map { it.daredToId }
            var participants: List<User> = userRepository.getUsersByIds(participantsIds)

            if (challenge == null) {
                _challengeDetails.value = null
                return@launch
            }

            _challengeDetails.value = ChallengeDetails(
                id = challengeId,
                name = challenge.title,
                imageUrl = challenge.pictureUrl,
                description = challenge.description,
                deadline = challenge.deadlineDate.toString(),
                type = challenge.type,
                goal = challenge.goal,
                participants = participants.map { Participant(it.userId,"${it.firstName} ${it.lastName}", it.profilePictureUrl) }
            )

        }
    }
}





