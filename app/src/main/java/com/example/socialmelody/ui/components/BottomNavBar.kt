package com.example.socialmelody.ui.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.socialmelody.data.Screen

@Composable
fun BottomNavBar(navController: NavController) {
    val screens = listOf(Screen.Home, Screen.NowPlaying, Screen.Library, Screen.Search, Screen.Settings)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = { Icon(Icons.Default.Home, contentDescription = screen.route) },
                label = { Text(screen.route.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}