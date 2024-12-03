package com.example.fitbuddies

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitbuddies.ui.components.BottomNavigationBar
import com.example.fitbuddies.ui.components.NavigationItem
import com.example.fitbuddies.ui.screens.*
import com.example.fitbuddies.ui.theme.FitBuddiesTheme
import com.example.fitbuddies.viewmodels.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitBuddiesTheme {
                var showOnboarding by remember { mutableStateOf(false) } // TODO: Save onboarding state shared preferences and change to true

                if (showOnboarding) {
                    OnboardingScreen(
                        onFinishOnboarding = {
                            showOnboarding = false
                        }
                    )
                } else {
                    MainScreen()
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var currentRoute by remember { mutableStateOf(NavigationItem.Home.route) }
    val homeViewModel = viewModel<HomeViewModel>()
    val friendsViewModel = viewModel<FitBuddiesViewModel>()
    val challengesViewModel = viewModel<ChallengesViewModel>()
    val profileViewModel = viewModel<ProfileViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = "Fitness",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${profileViewModel.user.value.firstName} ${profileViewModel.user.value.lastName}",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement settings */ }) {
                        Icon(Icons.Default.QrCodeScanner, contentDescription = "QR Code Scanner")
                    }
                    IconButton(onClick = { /* TODO: Implement notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { /* TODO: Implement settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { navigationItem ->
                    currentRoute = navigationItem.route
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            when (currentRoute) {
                NavigationItem.Home.route -> HomeScreen(homeViewModel)
                NavigationItem.Friends.route -> FitBuddiesScreen(friendsViewModel)
                NavigationItem.Challenges.route -> ChallengesScreen(challengesViewModel)
                NavigationItem.Profile.route -> ProfileScreen(profileViewModel)
                NavigationItem.Add.route -> AddChallengeScreen()
                else -> HomeScreen(homeViewModel)
            }
        }
    }
}
