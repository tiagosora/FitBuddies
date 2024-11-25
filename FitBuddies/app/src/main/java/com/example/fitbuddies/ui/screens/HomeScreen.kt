package com.example.fitbuddies.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            item {
                Text(
                    "Active Challenges",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
            }
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
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(150.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(challenge.title, style = MaterialTheme.typography.titleMedium)
            Text(challenge.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun FriendActivityItem(activity: FriendActivity) {
    ListItem(
        headlineContent = { Text(activity.friendName) },
        supportingContent = { Text(activity.activityDescription) },
        leadingContent = {
            // TODO: Add friend avatar
        }
    )
}