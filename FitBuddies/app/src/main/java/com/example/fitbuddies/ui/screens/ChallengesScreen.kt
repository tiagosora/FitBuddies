package com.example.fitbuddies.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fitbuddies.viewmodels.ChallengesViewModel
import org.burnoutcrew.reorderable.*

@Composable
fun ChallengesScreen(viewModel: ChallengesViewModel) {
    val pendingChallenges by viewModel.pendingChallenges.collectAsState()
    val acceptedChallenges by viewModel.acceptedChallenges.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Challenges",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Pending Challenges",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(pendingChallenges) { challenge ->
                PendingChallengeItem(
                    challenge = challenge,
                    onAccept = {},
                    onDeny = {},
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Accepted Challenges",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        val reorderState = rememberReorderableLazyListState(onMove = { from, to ->
            viewModel.reorderAcceptedChallenges(from.index, to.index)
        })

        LazyColumn(
            state = reorderState.listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .reorderable(reorderState)
                .detectReorderAfterLongPress(reorderState)
        ) {
            items(acceptedChallenges) { challenge ->
                ReorderableItem(reorderableState = reorderState, key = challenge.title) { isDragging ->
                    val elevation = if (isDragging) 16.dp else 0.dp
                    AcceptedChallengeItem(
                        challenge = challenge,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .shadow(elevation),
                        elevation = elevation
                    )
                }
            }
        }
    }
}

@Composable
fun PendingChallengeItem(
    challenge: ChallengesViewModel.Challenge,
    onAccept: () -> Unit,
    onDeny: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .width(280.dp)
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    imageVector = when (challenge.type) {
                        "Running" -> Icons.AutoMirrored.Filled.DirectionsRun
                        "Cycling" -> Icons.AutoMirrored.Filled.DirectionsBike
                        "Weightlifting" -> Icons.Default.FitnessCenter
                        else -> Icons.Default.SportsHandball
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = challenge.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Accept")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Accept")
                }
                Button(
                    onClick = onDeny,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Deny")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Deny")
                }
            }
        }
    }
}

@Composable
fun AcceptedChallengeItem(
    challenge: ChallengesViewModel.Challenge,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DragIndicator,
                contentDescription = "Drag to reorder",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = challenge.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = challenge.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
        }
    }
}

