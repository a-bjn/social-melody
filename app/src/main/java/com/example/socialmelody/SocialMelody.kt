package com.example.socialmelody

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.socialmelody.ui.theme.navigation.Screen
import com.example.socialmelody.ui.theme.screens.LoginScreen
import com.example.socialmelody.ui.theme.screens.MainScreen


@Composable
fun SocialMelody() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Main.route) { MainScreen(navController) }
    }
}