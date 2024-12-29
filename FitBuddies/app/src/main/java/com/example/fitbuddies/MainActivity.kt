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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitbuddies.data.models.User
import com.example.fitbuddies.ui.components.BottomNavigationBar
import com.example.fitbuddies.ui.components.NavigationItem
import com.example.fitbuddies.ui.screens.*
import com.example.fitbuddies.ui.theme.FitBuddiesTheme
import com.example.fitbuddies.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isOnboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false)

        setContent {
            FitBuddiesTheme {
                var currentScreen by remember { mutableStateOf(if (isOnboardingCompleted) "sign in" else "onboarding") }

//                var isAuthenticated by remember { mutableStateOf(false) }
                var isAuthenticated by remember { mutableStateOf(true) }
                sharedPreferences.edit().putString("currentUserId", "2eef40fc-3223-4bb2-8003-6af0e45dfc53").apply()

                val authenticationViewModel = viewModel<AuthenticationViewModel>()

                fun onSignIn(email: String, password: String) = runBlocking {
                    CoroutineScope(Dispatchers.IO).launch {
                        val response: User? = authenticationViewModel.signIn(email, password)
                        withContext(Dispatchers.Main) {
                            if (response != null) {
                                isAuthenticated = true
                                sharedPreferences.edit().putString("currentUserId", response.userId).apply()
                            }
                        }
                    }
                    println("isAuthenticated: $isAuthenticated")
                    println("currentScreen: $currentScreen")
                }

                fun onSignUp(firstName: String, lastName: String, email: String, password: String) = runBlocking {
                    CoroutineScope(Dispatchers.IO).launch {
                        val response: User? = authenticationViewModel.signUp(firstName, lastName, email, password)
                        withContext(Dispatchers.Main) {
                            if (response != null) {
                                isAuthenticated = true
                                sharedPreferences.edit().putString("currentUserId", response.userId).apply()
                            }
                        }
                    }
                    println("isAuthenticated: $isAuthenticated")
                    println("currentScreen: $currentScreen")
                }

                fun completeOnboarding() {
                    sharedPreferences.edit().putBoolean("onboarding_completed", true).apply()
                    currentScreen = "sign in"
                }

                when {
                    !isAuthenticated -> {
                        when (currentScreen) {
                            "onboarding" -> {
                                OnboardingScreen { completeOnboarding() }
                            }
                            "sign in" -> {
                                SignInScreen(
                                    onSignIn = { email, password -> onSignIn(email, password) },
                                    onNavigateToSignUp = { currentScreen = "sign up" }
                                )
                            }
                            "sign up" -> {
                                SignUpScreen(
                                    onSignUp = { firstName, lastName, email, password -> onSignUp(firstName, lastName, email, password) },
                                    onNavigateToSignIn = { currentScreen = "sign in" }
                                )
                            }
                            else -> {
                                currentScreen = "onboarding"
                            }
                        }
                    }
                    else -> {
                        MainScreen()
                    }
                }
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    var currentRoute by remember { mutableStateOf(NavigationItem.Home.route) }
    val user by profileViewModel.user.collectAsState()

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
                            text = "${user?.firstName} ${user?.lastName}",
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
                .padding(
                    start = 32.dp,
                    end = 32.dp,
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            when (currentRoute) {
                NavigationItem.Home.route -> HomeScreen()
                NavigationItem.Friends.route -> FitBuddiesScreen()
                NavigationItem.Challenges.route -> ChallengesScreen()
                NavigationItem.Profile.route -> ProfileScreen()
                NavigationItem.Add.route -> AddChallengeScreen()
                else -> HomeScreen()
            }
        }
    }
}
