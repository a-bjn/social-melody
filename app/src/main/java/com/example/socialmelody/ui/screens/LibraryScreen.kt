package com.example.socialmelody.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.socialmelody.api.SpotifyApiController
import com.example.socialmelody.data.Album
import com.example.socialmelody.data.Playlist
import com.example.socialmelody.data.UserProfile
import com.example.socialmelody.ui.components.AlbumItem
import com.example.socialmelody.ui.components.LibraryHeader
import com.example.socialmelody.ui.components.PlaylistItem
import com.example.socialmelody.utils.getAccessToken

@Composable
fun LibraryScreen(navController: NavController) {
    val context = LocalContext.current
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var playlists by remember { mutableStateOf<List<Playlist>>(emptyList()) }
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }
    val accessToken = getAccessToken(context) // Implement getAccessToken as shown below

    // Fetch API data when the composable enters composition.
    LaunchedEffect(accessToken) {
        accessToken?.let { token ->
            SpotifyApiController.fetchUserProfile(
                accessToken = token,
                onSuccess = { profile -> userProfile = profile },
                onError = { error -> errorMessage = error }
            )
            SpotifyApiController.fetchPlaylists(
                accessToken = token,
                onSuccess = { fetchedPlaylists -> playlists = fetchedPlaylists },
                onError = { error -> errorMessage = error }
            )
            SpotifyApiController.fetchAlbums(
                accessToken = token,
                onSuccess = { fetchedAlbums -> albums = fetchedAlbums },
                onError = { error -> errorMessage = error }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header: display user profile and title.
        userProfile?.let { profile ->
            LibraryHeader(userProfile = profile)
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Section: Playlists
        Text(
            text = "Playlists",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(playlists) { playlist ->
                PlaylistItem(playlist = playlist) {
                    // Navigate to playlist detail screen.
                    navController.navigate("playlist_detail/${playlist.id}")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Section: Albums
        Text(
            text = "Albums",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(albums) { album ->
                AlbumItem(album = album) {
                    // Navigate to album detail screen.
                    navController.navigate("album_detail/${album.id}")
                }
            }
        }
    }
}
