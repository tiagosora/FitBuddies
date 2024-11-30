package com.example.fitbuddies.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitbuddies.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val user by profileViewModel.user.collectAsState()
    val challenges by profileViewModel.challenges.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            ProfileHeader(
                name = "${user.firstName} ${user.lastName}",
                email = user.email,
                profilePictureUrl = user.profilePictureUrl ?: ""
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth()
            ){
                TextButton(
                    onClick = { /* TODO: Implement edit profile functionality */ },
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profile")
                }
            }
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item {
            Text(
                "Fitness Stats",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item {
            FitnessStatsSection(
                challengesCompleted = user.challengesCompleted,
                distanceTraveled = user.distanceTraveled,
                caloriesBurned = user.caloriesBurned
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item {
            FitnessGoalsSection(
                profileViewModel = profileViewModel,
                challengesCompleted = user.challengesCompleted,
                distanceTraveled = user.distanceTraveled,
                caloriesBurned = user.caloriesBurned
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item {
            Text(
                "Recent Challenges",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        items(challenges) { challenge ->
            ChallengeItem(challenge)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProfileHeader(name: String, email: String, profilePictureUrl: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO: Replace with actual image loading using Coil
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FitnessStatsSection(challengesCompleted: Int, distanceTraveled: Double, caloriesBurned: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(
            icon = Icons.Default.FitnessCenter,
            value = challengesCompleted.toString(),
            label = "Workouts",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            value = "%.1f km".format(distanceTraveled),
            label = "Distance",
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = Icons.Default.LocalFireDepartment,
            value = caloriesBurned.toString(),
            label = "Calories",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp) // Icon size
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = true
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                softWrap = true
            )
        }
    }
}

@Composable
fun FitnessGoalsSection(profileViewModel: ProfileViewModel, challengesCompleted: Int, distanceTraveled: Double, caloriesBurned: Int) {
    val nextChallengesGoal: Int = profileViewModel.challengesGoals.firstOrNull { it > challengesCompleted } ?: profileViewModel.challengesGoals.last()
    val nextDistanceGoal: Int = profileViewModel.distanceGoals.firstOrNull { it > distanceTraveled } ?: profileViewModel.distanceGoals.last()
    val nextCaloriesGoal: Int = profileViewModel.caloriesGoals.firstOrNull { it > caloriesBurned } ?: profileViewModel.caloriesGoals.last()
    
    Column(modifier = Modifier.fillMaxWidth()) {
        GoalProgressBar(
            icon = Icons.Default.FitnessCenter,
            current = challengesCompleted,
            goal = nextChallengesGoal,
            label = "$challengesCompleted/$nextChallengesGoal workouts"
        )
        Spacer(modifier = Modifier.height(8.dp))
        GoalProgressBar(
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            current = distanceTraveled.toInt(),
            goal = nextDistanceGoal,
            label = "%.1f/$nextDistanceGoal km".format(distanceTraveled)
        )
        Spacer(modifier = Modifier.height(8.dp))
        GoalProgressBar(
            icon = Icons.Default.LocalFireDepartment,
            current = caloriesBurned,
            goal = nextCaloriesGoal,
            label = "$caloriesBurned/$nextCaloriesGoal calories"
        )
    }
}

@Composable
fun GoalProgressBar(icon: ImageVector, current: Int, goal: Int, label: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            LinearProgressIndicator(
                progress = { (current.toFloat() / goal).coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ChallengeItem(challenge: ProfileViewModel.Challenge) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (challenge.type) {
                    "Running" -> Icons.AutoMirrored.Filled.DirectionsRun
                    "Cycling" -> Icons.AutoMirrored.Filled.DirectionsBike
                    "Weightlifting" -> Icons.Default.FitnessCenter
                    else -> Icons.Default.SportsHandball
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = challenge.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = challenge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = challenge.date,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

