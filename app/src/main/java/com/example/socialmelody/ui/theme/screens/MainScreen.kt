package com.example.socialmelody.ui.theme.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.socialmelody.ui.theme.components.BottomNavBar
import com.example.socialmelody.ui.theme.navigation.Screen

@Composable
fun MainScreen(navController: NavController) {
    val innerNavController = rememberNavController() // Inner navigation for bottom bar

    Scaffold(
        bottomBar = { BottomNavBar(innerNavController) } // Bottom navigation bar
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(innerNavController) }
            composable(Screen.NowPlaying.route) { NowPlayingScreen() }
            composable(Screen.Search.route) { SearchScreen() }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
        }
    }
}