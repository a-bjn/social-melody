package com.example.socialmelody.api

import android.util.Log
import com.example.socialmelody.data.Playlist
import com.example.socialmelody.data.RecentTrack
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object SpotifyApiController {
    private val client = OkHttpClient()

    /**
     * Sends a play command to Spotify to start playback of the specified track.
     * Note: Playback will occur on an active Spotify device.
     *
     * @param accessToken A valid Spotify access token with the "user-modify-playback-state" scope.
     * @param trackUri The Spotify URI of the track to play (e.g., "spotify:track:4cOdK2wGLETKBW3PvgPWqT").
     */
    fun playSong(accessToken: String, trackUri: String) {
        val url = "https://api.spotify.com/v1/me/player/play"

        // Create a JSON body with the track URI
        val jsonBody = JSONObject().apply {
            put("uris", JSONArray(listOf(trackUri)))
        }

        val requestBody = jsonBody.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .addHeader("Content-Type", "application/json")
            .put(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("SpotifyApiController", "Failed to send play command: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("SpotifyApiController", "Playback started successfully!")
                } else {
                    Log.e("SpotifyApiController", "Error starting playback: ${response.body?.string()}")
                }
            }
        })
    }

    /**
     * Fetches the user's playlists from Spotify.
     *
     * @param accessToken A valid Spotify access token with the "playlist-read-private" scope.
     * @param onSuccess Callback with a list of playlists on success.
     * @param onError Callback with an error message on failure.
     */
    fun fetchPlaylists(
        accessToken: String,
        onSuccess: (List<Playlist>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "https://api.spotify.com/v1/me/playlists"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onError(response.body?.string() ?: "Error fetching playlists")
                    return
                }
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    try {
                        val json = JSONObject(responseBody)
                        val items: JSONArray = json.getJSONArray("items")
                        val playlists = mutableListOf<Playlist>()
                        for (i in 0 until items.length()) {
                            val item = items.getJSONObject(i)
                            val id = item.getString("id")
                            val name = item.getString("name")
                            // Get the first image if available
                            val images = item.optJSONArray("images")
                            val imageUrl = if (images != null && images.length() > 0)
                                images.getJSONObject(0).getString("url")
                            else null

                            playlists.add(Playlist(id, name, imageUrl))
                        }
                        onSuccess(playlists)
                    } catch (e: Exception) {
                        onError(e.message ?: "Parsing error")
                    }
                } else {
                    onError("Empty response")
                }
            }
        })
    }

    /**
     * Fetches the user's recently played tracks from Spotify.
     *
     * @param accessToken A valid Spotify access token with the "user-read-recently-played" scope.
     * @param onSuccess Callback with a list of recently played items on success.
     * @param onError Callback with an error message on failure.
     */
    fun fetchRecentlyPlayed(
        accessToken: String,
        onSuccess: (List<RecentTrack>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "https://api.spotify.com/v1/me/player/recently-played?limit=8"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onError(response.body?.string() ?: "Error fetching recently played items")
                    return
                }
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    try {
                        val json = JSONObject(responseBody)
                        val items = json.getJSONArray("items")
                        val recentItems = mutableListOf<RecentTrack>()
                        for (i in 0 until items.length()) {
                            val item = items.getJSONObject(i)
                            val playedAt = item.getString("played_at")
                            val track = item.getJSONObject("track")
                            val trackId = track.getString("id")
                            val trackName = track.getString("name")
                            val album = track.getJSONObject("album")
                            val albumName = album.getString("name")
                            val images = album.optJSONArray("images")
                            val albumImageUrl = if (images != null && images.length() > 0)
                                images.getJSONObject(0).getString("url")
                            else null
                            recentItems.add(
                                RecentTrack(
                                    trackId = trackId,
                                    trackName = trackName,
                                    albumName = albumName,
                                    albumImageUrl = albumImageUrl,
                                    playedAt = playedAt
                                )
                            )
                        }
                        onSuccess(recentItems)
                    } catch (e: Exception) {
                        onError(e.message ?: "Parsing error")
                    }
                } else {
                    onError("Empty response")
                }
            }
        })
    }

    // Additional functions (e.g., pauseSong, skipSong, fetchUserProfile) can be added here.
}
