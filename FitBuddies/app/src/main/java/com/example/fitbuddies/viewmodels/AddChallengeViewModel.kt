package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddChallengeViewModel : ViewModel() {
    private val _creationStatus = MutableStateFlow<CreationStatus>(CreationStatus.Idle)
    val creationStatus: StateFlow<CreationStatus> = _creationStatus.asStateFlow()

    fun createChallenge(title: String, description: String, type: String, duration: Int) {
        viewModelScope.launch {
            _creationStatus.value = CreationStatus.Loading
            try {
                // TODO: Implement challenge creation logic
                // This is where you would typically make an API call or update local storage
                // For now, we'll just simulate a delay
                kotlinx.coroutines.delay(1000)
                _creationStatus.value = CreationStatus.Success
            } catch (e: Exception) {
                _creationStatus.value = CreationStatus.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class CreationStatus {
        data object Idle : CreationStatus()
        data object Loading : CreationStatus()
        data object Success : CreationStatus()
        data class Error(val message: String) : CreationStatus()
    }
}
