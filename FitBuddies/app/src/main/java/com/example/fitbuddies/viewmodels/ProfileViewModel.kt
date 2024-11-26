package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {

    private val _profile = MutableStateFlow(Profile("John Doe", "john.doe@example", "1234567890"))
    val profile: StateFlow<Profile> = _profile

}

class Profile(
    val name: String,
    val email: String,
    val phoneNumber: String
)