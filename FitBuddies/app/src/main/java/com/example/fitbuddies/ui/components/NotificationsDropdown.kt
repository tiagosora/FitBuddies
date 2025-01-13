package com.example.fitbuddies.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fitbuddies.R
import com.example.fitbuddies.data.models.Notification
import com.example.fitbuddies.viewmodels.NotificationsViewModel

@Composable
fun NotificationsDropdown(
    notificationsViewModel: NotificationsViewModel = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val notifications by notificationsViewModel.notifications.collectAsState(initial = emptyList())

    Surface(
        modifier = Modifier
            .width(300.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )

            Divider()

            if (notifications.isEmpty()) {
                Text(
                    text = "No notifications at the moment",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationDropdownItem(
                            notification = notification,
                            onRemove = { notificationsViewModel.removeNotification(it) }
                        )
                        Divider()
                    }
                }
            }

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("See All")
            }
        }
    }
}

@Composable
fun NotificationDropdownItem(notification: Notification, onRemove: (Notification) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            Text(
                text = "Challenge: ${notification.challengeId}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = notification.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = notification.status,
            style = MaterialTheme.typography.bodySmall,
            color = if (notification.status == "Success") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(onClick = { onRemove(notification) }) {
            Icon(Icons.Default.Close, contentDescription = "Remove")
        }
    }
}
