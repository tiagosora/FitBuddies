package com.example.fitbuddies.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

sealed class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    data object Home : NavigationItem("home", Icons.Default.Home, "Home")
    data object Friends : NavigationItem("friends", Icons.Default.Person, "Friends")
    data object Challenges : NavigationItem("dares", Icons.Default.Menu, "Dares")
    data object Profile : NavigationItem("profile", Icons.Default.AccountCircle, "Profile")
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (NavigationItem) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        FloatingActionButton(
            onClick = { /* TODO: Create new dare */ },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-32).dp)
                .zIndex(1f).size(64.dp),

            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Create new dare",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 8.dp
        ) {
            val items = listOf(
                NavigationItem.Home,
                NavigationItem.Friends,
                NavigationItem.Challenges,
                NavigationItem.Profile
            )

            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    selected = currentRoute == item.route,
                    onClick = { onNavigate(item) },
                    modifier = Modifier.padding(
                        end = if (index == 1) 32.dp else 0.dp,
                        start = if (index == 2) 32.dp else 0.dp
                    )
                )
            }
        }
    }
}