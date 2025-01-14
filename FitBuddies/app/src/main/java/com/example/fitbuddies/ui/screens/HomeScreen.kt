package com.example.fitbuddies.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SportsHandball
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fitbuddies.viewmodels.HomeViewModel
import com.example.fitbuddies.viewmodels.HomeViewModel.ActiveChallenge
import com.example.fitbuddies.viewmodels.HomeViewModel.FitBuddyChallenge
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val tabTitles = listOf("Your Activity", "Feed")

    // Controla a quantidade de páginas (abas) e a página inicial
    val pagerState = rememberPagerState(
        pageCount = { tabTitles.size }, // ou pageCount = tabTitles.size
        initialPage = 0
    )

    val scope = rememberCoroutineScope()

    // Layout principal contendo o TabRow + HorizontalPager
    Column(modifier = Modifier.fillMaxSize()) {
        // Barra de abas no topo
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = (pagerState.currentPage == index),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title) }
                )
            }
        }

        // Pager (deslizar entre “Your Activity” e “Feed”)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> {
                    // Aba 1 → Conteúdo da “Your Activity”
                    YourActivityTab(navController, homeViewModel)
                }
                1 -> {
                    // Aba 2 → Conteúdo do “Feed”
                    // (Definido em outro arquivo, ex: FeedTab.kt)
                    FeedTab()
                }
            }
        }
    }
}

@Composable
fun YourActivityTab(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {
    val activeChallenges by homeViewModel.activeChallenges.collectAsState()
    val fitBuddiesChallenges by homeViewModel.fitBuddiesChallenges.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Adicionamos um espaço antes de exibir a DailyActivitySummary
        item {
            Spacer(modifier = Modifier.height(24.dp))  // <<--- Espaço extra
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
                    ActiveChallengeCard(activeChallenge, navController)
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Text(
                "FitBuddies Challenges",
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
fun DailyActivitySummary(
    homeViewModel: HomeViewModel,
) {
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
fun ActiveChallengeCard(
    challenge: ActiveChallenge,
    navController: NavHostController,
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(180.dp)
            .clickable {
                val id = challenge.challengeId
                navController.navigate("challenge_details/$id")
            },
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
                progress = challenge.completionRate,
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
        headlineContent = {
            Text(
                fitBuddyChallenge.fitBuddyName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        },
        supportingContent = {
            Text(
                fitBuddyChallenge.lastChallengeTitle,
                style = MaterialTheme.typography.bodySmall
            )
        },
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
