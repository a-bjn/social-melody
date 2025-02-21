package com.example.socialmelody.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.socialmelody.auth.SpotifyAuthManager

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val spotifyAuth = remember { SpotifyAuthManager(context) }
    var loginError by remember { mutableStateOf<String?>(null) }

    val authLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            spotifyAuth.handleAuthResponse(data, navController) // ✅ Handle login & navigate
        } else {
            loginError = "Login failed"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to SocialMelody!", fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Login with Spotify to continue", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { spotifyAuth.startLogin { authLauncher.launch(it) } }
        ) {
            Text("Login with Spotify")
        }

        loginError?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Error: $it", color = Color.Red, fontSize = 14.sp)
        }
    }
}
