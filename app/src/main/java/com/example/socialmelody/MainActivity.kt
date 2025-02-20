package com.example.socialmelody

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.socialmelody.ui.auth.SpotifyAuthManager
import com.example.socialmelody.ui.theme.SocialMelodyTheme

class MainActivity : ComponentActivity() {
    private lateinit var spotifyAuth: SpotifyAuthManager
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        spotifyAuth = SpotifyAuthManager(this)
        setContent {
            navController = rememberNavController()
            SocialMelodyTheme {
                SocialMelody(navController)
            }
        }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.data != null) {
            val uri: Uri = intent.data!!
            Log.d("SpotifyAuth", "Received Redirect URI: $uri")
            spotifyAuth.handleAuthResponse(intent, navController)
        }
    }
}
