package com.example.socialmelody.data

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Main : Screen("main")
    data object Home : Screen("home")
    data object NowPlaying : Screen("now_playing")
    data object Library : Screen("library")
    data object Search : Screen("search")
    data object Settings : Screen("settings")
}
