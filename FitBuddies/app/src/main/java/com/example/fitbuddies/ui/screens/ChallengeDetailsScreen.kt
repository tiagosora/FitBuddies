package com.example.fitbuddies.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ChallengeDetailsViewModel
import ChallengeDetails
import Participant
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ChallengeDetailsScreen(
    viewModel: ChallengeDetailsViewModel = hiltViewModel(),
    challengeId: String,
    navController: NavController
) {
    val challengeDetails by viewModel.challengeDetails.collectAsStateWithLifecycle()

    LaunchedEffect(challengeId) {
        if (challengeId.isNotEmpty()) {
            viewModel.loadChallengeDetails(challengeId)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, start = 16.dp, end = 16.dp)
    ) {
        item {
            QRCodeSection(onQRCodeClick = { /* Simulated QR Code click */ })
        }

        if(challengeDetails?.type == "Running" || challengeDetails?.type == "Cycling"){
            item { StartMapSection(onStartExercise = {}, navController) }}
        else{
            item { StartExerciseSection(onStartExercise = { elapsedTime -> println("Tempo decorrido: $elapsedTime segundos")})   }

        }



        if (challengeDetails != null) {
            item {
                ChallengeInfoCard(challengeDetails!!)
            }
            item {
                ChallengeDetailsAlignedSection(challengeDetails!!)
            }
            item {
                Text(
                    "Participants",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            items(challengeDetails!!.participants) { participant ->
                ParticipantItem(participant)
            }
        } else {
            item {
                Text(
                    text = "Static Challenge Details",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun ChallengeDetailsAlignedSection(details: ChallengeDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            DetailCard("Duration", details.duration)
            Spacer(modifier = Modifier.height(20.dp))
            DetailCard(
                "Goal",
                if (details.type == "Running" || details.type == "Cycling") {
                    "${details.goalCompletion}/${details.goal} km"
                } else {
                    "${details.goalCompletion}/${details.goal} h"
                }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Card(
            modifier = Modifier
                .weight(1f)
                .height(IntrinsicSize.Max),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = details.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 7,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@Composable
fun QRCodeSection(onQRCodeClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable(onClick = onQRCodeClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.QrCode,
                contentDescription = "QR Code",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                "Invite your friends",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun StartExerciseSection(onStartExercise: (Long) -> Unit) {
    val startTime = remember { mutableStateOf<Long?>(null) }
    val elapsedTime = remember { mutableStateOf(0L) } // TODO: Mandar isto para a API
    val isRunning = remember { mutableStateOf(false) }

    // Atualiza a contagem em tempo real enquanto está em execução
    LaunchedEffect(isRunning.value) {
        if (isRunning.value) {
            while (isRunning.value) {
                startTime.value?.let {
                    elapsedTime.value = (System.currentTimeMillis() - it) / 1000
                }
                kotlinx.coroutines.delay(1000L) // Atualiza a cada segundo
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Exibe o tempo formatado ou uma mensagem inicial
            Text(
                text = if (startTime.value == null) {
                    "Click Start to Begin Exercise" // Mensagem inicial
                } else {
                    formatTime(elapsedTime.value) // Tempo formatado
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Botão Play/Pause
                IconButton(onClick = {
                    if (isRunning.value) {
                        isRunning.value = false // Pausa o exercício
                    } else {
                        if (startTime.value == null) {
                            startTime.value = System.currentTimeMillis()
                        }
                        isRunning.value = true // Inicia ou retoma o exercício
                    }
                }) {
                    Icon(
                        imageVector = if (isRunning.value) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning.value) "Pause" else "Play",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }

                // Botão Stop
                IconButton(onClick = {
                    if (isRunning.value) {
                        isRunning.value = false // Para a contagem
                    }
                    startTime.value?.let {
                        elapsedTime.value = (System.currentTimeMillis() - it) / 1000
                        onStartExercise(elapsedTime.value)
                    }
                    startTime.value = null // Reseta o tempo
                    elapsedTime.value = 0L
                }) {
                    Icon(
                        imageVector = Icons.Default.Stop,
                        contentDescription = "Stop",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

fun formatTime(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
}

@Composable
fun StartMapSection(onStartExercise: (Long) -> Unit, navController: NavController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clickable { navController.navigate("route_map") },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription =  "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = "Click Start to Begin Exercise ",
                    style = MaterialTheme.typography.bodyLarge,
                )

        }
    }
} }


@Composable
fun ChallengeInfoCard(details: ChallengeDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(
                details.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ChallengeDetailsSection(details: ChallengeDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            DetailCard("Duration", details.duration)
            Spacer(modifier = Modifier.height(8.dp))
            DetailCard(
                "Goal",
                if (details.type == "running" || details.type == "cycling") {
                    "${details.goalCompletion}/${details.goal} km"
                } else {
                    "${details.goalCompletion}/${details.goal} h"
                }
            )        }
        Spacer(modifier = Modifier.width(16.dp))
        Card(
            modifier = Modifier
                .weight(1f)
                .height(IntrinsicSize.Max),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = details.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun DetailCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ParticipantItem(participant: Participant) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = participant.name,
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.secondary, shape = CircleShape),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                participant.name,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

