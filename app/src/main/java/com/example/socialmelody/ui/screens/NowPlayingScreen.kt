package com.example.socialmelody.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmelody.ui.api.playSong

@Composable
fun NowPlayingScreen() {
    val context = LocalContext.current
    val accessToken = remember { getAccessToken(context) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to SocialMelody!", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (accessToken != null) {
                playSong(accessToken, "spotify:track:4cOdK2wGLETKBW3PvgPWqT")
            } else {
                Log.e("SpotifyPlayback", "Access token is missing!")
            }
        }) {
            Text("Play Song")
        }
    }
}

fun getAccessToken(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("access_token", null)
}