package com.example.socialmelody.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.socialmelody.ui.components.BottomNavBar
import com.example.socialmelody.data.Screen

@Composable
fun MainScreen(navController: NavController) {
    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(innerNavController) }
    ) { innerPadding ->
        NavHost(
            navController = innerNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.NowPlaying.route) { NowPlayingScreen() }
            composable(Screen.Library.route) { LibraryScreen(navController) }
            composable(Screen.Search.route) { SearchScreen() }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
        }
    }
}