package com.example.fitbuddies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitbuddies.ui.components.BottomNavigationBar
import com.example.fitbuddies.ui.components.NavigationItem
import com.example.fitbuddies.ui.screens.HomeScreen
import com.example.fitbuddies.ui.screens.FriendsScreen
import com.example.fitbuddies.ui.screens.ProfileScreen
import com.example.fitbuddies.ui.screens.ChallengesScreen
import com.example.fitbuddies.ui.theme.FitBuddiesTheme
import com.example.fitbuddies.viewmodels.HomeViewModel

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

@Composable
fun MainScreen() {
    var currentRoute by remember { mutableStateOf(NavigationItem.Home.route) }
    val viewModel: HomeViewModel = viewModel()

    Scaffold(
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
                NavigationItem.Home.route -> HomeScreen(viewModel)
                NavigationItem.Friends.route -> FriendsScreen(viewModel)
                NavigationItem.Challenges.route -> ChallengesScreen(viewModel)
                NavigationItem.Profile.route -> ProfileScreen(viewModel)
                else -> HomeScreen(viewModel) // Temporary default
            }
        }
    }
}