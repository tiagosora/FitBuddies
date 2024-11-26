package com.example.fitbuddies

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitbuddies.ui.components.BottomNavigationBar
import com.example.fitbuddies.ui.components.NavigationItem
import com.example.fitbuddies.ui.screens.AddChallengeScreen
import com.example.fitbuddies.ui.screens.HomeScreen
import com.example.fitbuddies.ui.screens.FriendsScreen
import com.example.fitbuddies.ui.screens.ProfileScreen
import com.example.fitbuddies.ui.screens.ChallengesScreen
import com.example.fitbuddies.ui.theme.FitBuddiesTheme
import com.example.fitbuddies.viewmodels.ChallengesViewModel
import com.example.fitbuddies.viewmodels.FriendsViewModel
import com.example.fitbuddies.viewmodels.HomeViewModel
import com.example.fitbuddies.viewmodels.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitBuddiesTheme {
                MainScreen()
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
    val friendsViewModel = viewModel<FriendsViewModel>()
    val challengesViewModel = viewModel<ChallengesViewModel>()
    val profileViewModel = viewModel<ProfileViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hi, ${profileViewModel.profile.value.name}",
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRoute) {
                NavigationItem.Home.route -> HomeScreen(homeViewModel)
                NavigationItem.Friends.route -> FriendsScreen(friendsViewModel)
                NavigationItem.Challenges.route -> ChallengesScreen(challengesViewModel)
                NavigationItem.Profile.route -> ProfileScreen(profileViewModel)
                NavigationItem.Add.route -> AddChallengeScreen()
                else -> HomeScreen(homeViewModel) // TODO: Temporary default
            }
        }
    }
}