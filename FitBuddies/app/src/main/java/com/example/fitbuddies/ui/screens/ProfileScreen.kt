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
    val profile by profileViewModel.profile.collectAsState()
    val recentActivities by profileViewModel.recentActivities.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            ProfileHeader(
                name = profile.name,
                email = profile.email,
                profilePicUrl = profile.profilePicUrl
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
                totalWorkouts = profile.totalWorkouts,
                totalDistance = profile.totalDistance,
                totalCaloriesBurned = profile.totalCaloriesBurned
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item {
            Text(
                "Fitness Goals",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item {
            FitnessGoalsSection(
                weeklyWorkoutGoal = profile.weeklyWorkoutGoal,
                weeklyDistanceGoal = profile.weeklyDistanceGoal,
                weeklyCalorieGoal = profile.weeklyCalorieGoal
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item {
            Text(
                "Recent Activities",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        items(recentActivities) { activity ->
            ActivityItem(activity)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProfileHeader(name: String, email: String, profilePicUrl: String) {
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
fun FitnessStatsSection(totalWorkouts: Int, totalDistance: Float, totalCaloriesBurned: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between cards
    ) {
        StatCard(
            icon = Icons.Default.FitnessCenter,
            value = totalWorkouts.toString(),
            label = "Workouts",
            modifier = Modifier.weight(1f) // Occupies 1/3 of the row width
        )
        StatCard(
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            value = "%.1f km".format(totalDistance),
            label = "Distance",
            modifier = Modifier.weight(1f) // Occupies 1/3 of the row width
        )
        StatCard(
            icon = Icons.Default.LocalFireDepartment,
            value = totalCaloriesBurned.toString(),
            label = "Calories",
            modifier = Modifier.weight(1f) // Occupies 1/3 of the row width
        )
    }
}

@Composable
fun StatCard(icon: ImageVector, value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(4.dp), // Add spacing around each card
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
fun FitnessGoalsSection(weeklyWorkoutGoal: Int, weeklyDistanceGoal: Float, weeklyCalorieGoal: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        GoalProgressBar(
            icon = Icons.Default.FitnessCenter,
            current = weeklyWorkoutGoal,
            goal = 7,
            label = "$weeklyWorkoutGoal/7 workouts"
        )
        Spacer(modifier = Modifier.height(8.dp))
        GoalProgressBar(
            icon = Icons.AutoMirrored.Filled.DirectionsRun,
            current = weeklyDistanceGoal.toInt(),
            goal = 50,
            label = "%.1f/50 km".format(weeklyDistanceGoal)
        )
        Spacer(modifier = Modifier.height(8.dp))
        GoalProgressBar(
            icon = Icons.Default.LocalFireDepartment,
            current = weeklyCalorieGoal,
            goal = 3500,
            label = "$weeklyCalorieGoal/3500 calories"
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
fun ActivityItem(activity: ProfileViewModel.Activity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (activity.type) {
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
                    text = activity.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = activity.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = activity.duration,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

