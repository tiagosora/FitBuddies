package com.example.fitbuddies.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fitbuddies.data.models.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import java.util.UUID

class NotificationsViewModel @Inject constructor() : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    fun addNotification(challengeId: String, title: String, description: String, status: String) {
        val newNotification = Notification(
            notificationId = UUID.randomUUID().toString(),
            challengeId = challengeId,
            title = title,
            description = description,
            status = status
        )
        _notifications.update { currentNotifications ->
            currentNotifications + newNotification
        }
    }

    fun removeNotification(notification: Notification) {
        _notifications.update { currentNotifications ->
            currentNotifications.filter { it.notificationId != notification.notificationId }
        }
    }
}
