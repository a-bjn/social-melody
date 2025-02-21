package com.example.socialmelody

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.socialmelody.data.Screen
import com.example.socialmelody.ui.screens.LoginScreen
import com.example.socialmelody.ui.screens.MainScreen

@Composable
fun SocialMelody(navController: NavHostController) {
    val context = LocalContext.current
    val isLoggedIn = remember { mutableStateOf(checkLoginState(context)) }

    LaunchedEffect(isLoggedIn.value) {
        if (isLoggedIn.value) {
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Main.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Main.route) { MainScreen(navController) }
    }
}

fun checkLoginState(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("access_token", null) != null
}
