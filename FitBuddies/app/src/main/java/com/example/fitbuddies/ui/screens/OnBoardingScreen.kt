package com.example.fitbuddies.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val illustration: @Composable () -> Unit
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            title = "Challenge Your Friends",
            description = "Create exciting fitness challenges and dare your friends to complete them. Make fitness fun and social!",
            illustration = { ChallengeIllustration() }
        ),
        OnboardingPage(
            title = "Track Your Progress",
            description = "Record your workouts, take photos and videos of your achievements, and share your fitness journey.",
            illustration = { TrackingIllustration() }
        ),
        OnboardingPage(
            title = "Join the Community",
            description = "Connect with fitness buddies, share achievements, and motivate each other to reach new heights.",
            illustration = { CommunityIllustration() }
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { position ->
                OnboardingPage(
                    page = pages[position],
                    isLastPage = position == pages.size - 1,
                    onFinish = onFinishOnboarding
                )
            }

            if (pagerState.currentPage < pages.size - 1) {

                // Page indicator
                Row(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pages.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration)
                            MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)

                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }

                // Skip button
                TextButton(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pages.size - 1)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Text(
                        "Skip",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    page: OnboardingPage,
    isLastPage: Boolean,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(72.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            page.illustration()
        }

        Text(
            text = page.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )

        Text(
            text = page.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(visible = isLastPage) {
            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Sign up",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        AnimatedVisibility(visible = !isLastPage) {
            Spacer(modifier = Modifier.height(140.dp))
        }

        AnimatedVisibility(visible = isLastPage) {
            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Log In",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ChallengeIllustration() {
    // Placeholder for custom illustration
    Box(
        modifier = Modifier
            .size(280.dp)
            .background(
                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "🏋️",
            fontSize = 80.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun TrackingIllustration() {
    Box(
        modifier = Modifier
            .size(280.dp)
            .background(
                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "📱",
            fontSize = 80.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun CommunityIllustration() {
    Box(
        modifier = Modifier
            .size(280.dp)
            .background(
                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f),
                RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "🤝",
            fontSize = 80.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}