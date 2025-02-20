package com.example.socialmelody.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.socialmelody.R  // your drawable resource

@Composable
fun NowPlayingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Album Art
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with your album art drawable
                contentDescription = "Album Art",
                modifier = Modifier
                    .size(300.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Song Info
            Text(text = "Song Title", fontSize = 24.sp, color = MaterialTheme.colorScheme.onBackground)
            Text(text = "Artist Name", fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))

            Spacer(modifier = Modifier.height(32.dp))

            // Playback Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { /* Handle previous */ }) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Previous",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(
                    onClick = { /* Handle play/pause */ },
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(onClick = { /* Handle next */ }) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
