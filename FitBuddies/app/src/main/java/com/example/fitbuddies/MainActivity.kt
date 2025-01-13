package com.example.fitbuddies

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.fitbuddies.ai.AiClient.generateFitnessChallenge
import com.example.fitbuddies.data.models.User
import com.example.fitbuddies.qr.QRCodeScannerWithPermission
import com.example.fitbuddies.ui.components.BottomNavigationBar
import com.example.fitbuddies.ui.components.NavigationItem
import com.example.fitbuddies.ui.screens.*
import com.example.fitbuddies.ui.theme.FitBuddiesTheme
import com.example.fitbuddies.viewmodels.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedPreferences: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isOnboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false)

//        val testAI = generateFitnessChallenge("Running")
//        Log.d("AI", testAI.toString())

        setContent {
            FitBuddiesTheme {
                val navController = rememberNavController()
                var currentScreen by remember {
                    mutableStateOf(
                        if (isOnboardingCompleted) "sign in" else "onboarding"
                    )
                }
                var isAuthenticated by remember { mutableStateOf(true) }

                // Exemplo fixo de currentUserId
                sharedPreferences.edit()
                    .putString("currentUserId", "2eef40fc-3223-4bb2-8003-6af0e45dfc53")
                    .apply()

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
                }

                fun completeOnboarding() {
                    sharedPreferences.edit().putBoolean("onboarding_completed", true).apply()
                    currentScreen = "sign in"
                }

                // Fluxo de telas de autenticação
                if (!isAuthenticated) {
                    when (currentScreen) {
                        "onboarding" -> OnboardingScreen { completeOnboarding() }
                        "sign in" -> SignInScreen(
                            onSignIn = { email, password -> onSignIn(email, password) },
                            onNavigateToSignUp = { currentScreen = "sign up" }
                        )
                        "sign up" -> SignUpScreen(
                            onSignUp = { firstName, lastName, email, password ->
                                onSignUp(firstName, lastName, email, password)
                            },
                            onNavigateToSignIn = { currentScreen = "sign in" }
                        )
                        else -> currentScreen = "onboarding"
                    }
                } else {
                    // Se já estiver autenticado, entra no app principal
                    MainApp(navController)
                }
            }
        }
    }
}

@Composable
fun MainApp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }

        composable(
            route = "challenge_details/{challengeId}",
            arguments = listOf(navArgument("challengeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val challengeId = backStackEntry.arguments?.getString("challengeId") ?: ""
            ChallengeDetailsScreen(challengeId = challengeId, navController = navController)
        }
        composable(
            route = "route_map",
        ) {
            RouteScreen(onPermissionDenied = {/* TODO: IMPLEMENTAR AÇÂO */},
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("scan_qr_code") {
            QRCodeScannerWithPermission { scannedUri ->
                try {
                    // Extrai apenas o ID do final da URI
                    val challengeId = scannedUri.substringAfterLast("/")
                    navController.navigate("challenge_details/$challengeId") {
                        // Opcional: configurações de navegação
                        popUpTo("scan_qr_code") { inclusive = true }
                    }
                } catch (e: Exception) {
                    Log.e("QRScanner", "Erro ao processar QR code: $scannedUri", e)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    navController: NavHostController,
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
                            text = "${user?.firstName ?: "Guest"} ${user?.lastName ?: ""}",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("scan_qr_code")
                    }) {
                        Icon(Icons.Default.QrCode, contentDescription = "Scan QR Code")
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { /* TODO */ }) {
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
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            when (currentRoute) {
                NavigationItem.Home.route -> HomeScreen(navController)
                NavigationItem.Friends.route -> FitBuddiesScreen()
                NavigationItem.Challenges.route -> ChallengesScreen()
                NavigationItem.Profile.route -> ProfileScreen()
                NavigationItem.Add.route -> AddChallengeScreen()
                else -> HomeScreen(navController)

            }
        }
    }
}
