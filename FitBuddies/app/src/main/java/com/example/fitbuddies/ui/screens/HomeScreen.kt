package com.example.fitbuddies.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.fitbuddies.viewmodels.Challenge
import com.example.fitbuddies.viewmodels.FriendActivity
import com.example.fitbuddies.viewmodels.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val challenges by viewModel.challenges.collectAsState()
    val friendActivities by viewModel.friendActivities.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // Section Title: Active Challenges
            item {
                Text(
                    "Active Challenges",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            // Challenges Row
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(challenges) { challenge ->
                        ChallengeCard(challenge)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            // Section Title: Friend Activities
            item {
                Text(
                    "Friend Activities",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(friendActivities) { activity ->
                FriendActivityItem(activity)
            }
        }
    }
}

@Composable
fun ChallengeCard(challenge: Challenge) {
    ElevatedCard(
        modifier = Modifier
            .width(200.dp)
            .height(160.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(challenge.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text(
                challenge.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun FriendActivityItem(activity: FriendActivity) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        headlineContent = { Text(activity.friendName, style = MaterialTheme.typography.titleSmall) },
        supportingContent = { Text(activity.activityDescription, style = MaterialTheme.typography.bodySmall) },
        leadingContent = {
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                // Placeholder for avatar
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}
