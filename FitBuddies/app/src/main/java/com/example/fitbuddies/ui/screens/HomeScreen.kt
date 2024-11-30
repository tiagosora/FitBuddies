package com.example.fitbuddies.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitbuddies.viewmodels.ActiveChallenge
import com.example.fitbuddies.viewmodels.FitBuddyChallenge
import com.example.fitbuddies.viewmodels.HomeViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val activeChallenges by homeViewModel.activeChallenges.collectAsState()
    val fitBuddiesChallenges by homeViewModel.fitBuddiesChallenges.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            DailyActivitySummary(homeViewModel)
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Text(
                "Active Challenges",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(activeChallenges) { activeChallenge ->
                    ActiveChallengeCard(activeChallenge)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Text(
                "FitBuddies Activities",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(fitBuddiesChallenges) { fitBuddyChallenge ->
            FitBuddyChallengeItem(fitBuddyChallenge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun DailyActivitySummary(homeViewModel: HomeViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Today's Activity",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    homeViewModel.dailySteps.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                contentDescription = "Steps",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ActiveChallengeCard(challenge: ActiveChallenge) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = when (challenge.type) {
                    "Running" -> Icons.AutoMirrored.Filled.DirectionsRun
                    "Cycling" -> Icons.AutoMirrored.Filled.DirectionsBike
                    "Exercise" -> Icons.Default.FitnessCenter
                    else -> Icons.Default.SportsHandball
                },
                contentDescription = challenge.type,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                challenge.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.Bold
            )
            Text(
                challenge.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            LinearProgressIndicator(
                progress = { challenge.completionRate },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )
        }
    }
}

@Composable
fun FitBuddyChallengeItem(fitBuddyChallenge: FitBuddyChallenge) {
    ListItem(
        headlineContent = { Text(fitBuddyChallenge.fitBuddyName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold) },
        supportingContent = { Text(fitBuddyChallenge.lastChallengeDescription, style = MaterialTheme.typography.bodySmall) },
        leadingContent = {
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = when (fitBuddyChallenge.lastChallengeType) {
                        "Running" -> Icons.AutoMirrored.Filled.DirectionsRun
                        "Cycling" -> Icons.AutoMirrored.Filled.DirectionsBike
                        "Weightlifting" -> Icons.Default.FitnessCenter
                        else -> Icons.Default.SportsHandball
                    },
                    contentDescription = fitBuddyChallenge.lastChallengeType,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}

