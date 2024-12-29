package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fitbuddies.data.models.User
import com.example.fitbuddies.data.repositories.UserRepository

class AuthenticationViewModel(
    val userRepository: UserRepository = UserRepository()
) : ViewModel()  {

    suspend fun signUp(firstName: String, lastName: String, email: String, password: String): User? {
        val user = User(
            email = email,
            password = password,
            firstName = firstName,
            lastName = lastName
        )
        val responseUser = userRepository.insertUser(user)
        if (responseUser != null) {
            return responseUser
        }
        return null
    }

    suspend fun signIn(email: String, password: String): User? {
        val responseUser = userRepository.authenticateUser(email, password)
        if (responseUser != null) {
            return responseUser
        }
        return null
    }
}