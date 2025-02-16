package com.example.socialmelody.ui.theme.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Main : Screen("main")
    data object Home : Screen("home")
    data object NowPlaying : Screen("now_playing")
    data object Search : Screen("search")
    data object Settings : Screen("settings")
}
