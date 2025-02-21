package com.example.socialmelody.ui.screens

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmelody.api.SpotifyApiController
import com.example.socialmelody.data.Playlist
import com.example.socialmelody.data.RecentTrack
import com.example.socialmelody.ui.components.PlaylistItem
import com.example.socialmelody.ui.components.RecentTrackItem

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val accessToken = remember { getAccessToken(context) }
    var playlists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var recentTracks by remember { mutableStateOf<List<RecentTrack>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(accessToken) {
        accessToken?.let { token ->
            SpotifyApiController.fetchPlaylists(
                accessToken = token,
                onSuccess = { fetchedPlaylists ->
                    playlists = fetchedPlaylists
                },
                onError = { error ->
                    errorMessage = error
                    Log.e("HomeScreen", "Error fetching playlists: $error")
                }
            )
            SpotifyApiController.fetchRecentlyPlayed(
                accessToken = token,
                onSuccess = { fetchedItems ->
                    recentTracks = fetchedItems
                },
                onError = { error ->
                    Log.e("HomeScreen", "Error fetching recently played: $error")
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "My Playlists",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(playlists) { playlist ->
                // Replace with your own composable to display a playlist card.
                PlaylistItem(playlist)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Recently Played",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(recentTracks) { item ->
                // Replace with your own composable to display a recently played item.
                RecentTrackItem(item)
            }
        }
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

fun getAccessToken(context: Context): String? {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("access_token", null)
}
