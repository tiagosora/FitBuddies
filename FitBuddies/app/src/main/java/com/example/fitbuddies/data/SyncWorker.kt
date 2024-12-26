package com.example.fitbuddies.data

import com.example.fitbuddies.data.repositories.ChallengeRepository
import com.example.fitbuddies.data.repositories.DareRepository
import com.example.fitbuddies.data.repositories.FriendshipRepository
import com.example.fitbuddies.data.repositories.UserRepository

class SyncWorker(
    private val challengeRepository: ChallengeRepository,
    private val dareRepository: DareRepository,
    private val friendshipRepository: FriendshipRepository,
    private val userRepository: UserRepository
) {

    suspend fun syncData() {
//        challengeRepository.refreshChallenges()
//        dareRepository.refreshDares()
//        friendshipRepository.refreshFriendships()
//        userRepository.refreshUsers()
    }
}
