package com.example.socialmelody.api

import android.util.Log
import com.example.socialmelody.data.Album
import com.example.socialmelody.data.Playlist
import com.example.socialmelody.data.RecentTrack
import com.example.socialmelody.data.UserProfile
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
     * Playback will occur on an active Spotify device.
     */
    fun playSong(accessToken: String, trackUri: String) {
        val url = "https://api.spotify.com/v1/me/player/play"

        // Create a JSON body with the track URI.
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
                            // Get the first image if available.
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
     * Fetches the user's saved albums from Spotify.
     *
     * Endpoint: GET https://api.spotify.com/v1/me/albums?limit=50
     * Requires the scope: "user-library-read"
     */
    fun fetchAlbums(
        accessToken: String,
        onSuccess: (List<Album>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "https://api.spotify.com/v1/me/albums?limit=50"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onError(response.body?.string() ?: "Error fetching albums")
                    return
                }
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    try {
                        val json = JSONObject(responseBody)
                        val items = json.getJSONArray("items")
                        val albums = mutableListOf<Album>()
                        for (i in 0 until items.length()) {
                            // Each item in the response contains an "album" object.
                            val item = items.getJSONObject(i)
                            val albumJson = item.getJSONObject("album")
                            val albumId = albumJson.getString("id")
                            val albumName = albumJson.getString("name")
                            val images = albumJson.optJSONArray("images")
                            val albumImageUrl = if (images != null && images.length() > 0)
                                images.getJSONObject(0).getString("url")
                            else null
                            albums.add(Album(albumId, albumName, albumImageUrl))
                        }
                        onSuccess(albums)
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
     * Fetches the user's profile from Spotify.
     *
     * Endpoint: GET https://api.spotify.com/v1/me
     * Requires the scope: "user-read-private" (and optionally "user-read-email")
     */
    fun fetchUserProfile(
        accessToken: String,
        onSuccess: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "https://api.spotify.com/v1/me"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onError(response.body?.string() ?: "Error fetching user profile")
                    return
                }
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    try {
                        val json = JSONObject(responseBody)
                        // "display_name" is the user's display name
                        val displayName = json.optString("display_name", "Unknown")
                        // "images" is an array; use the first image if available
                        val images = json.optJSONArray("images")
                        val imageUrl = if (images != null && images.length() > 0)
                            images.getJSONObject(0).getString("url")
                        else null
                        val userProfile = UserProfile(displayName, imageUrl)
                        onSuccess(userProfile)
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
     * Fetches the user's recently played albums by grouping the recently played tracks.
     *
     * This function calls the "Get Recently Played" endpoint and then groups tracks by album ID.
     * It returns a unique list of albums.
     *
     * @param accessToken A valid Spotify access token with the "user-read-recently-played" scope.
     * @param onSuccess Callback with a list of albums on success.
     * @param onError Callback with an error message on failure.
     */
    fun fetchRecentlyPlayedAlbums(
        accessToken: String,
        onSuccess: (List<Album>) -> Unit,
        onError: (String) -> Unit
    ) {
        // Increase limit to capture more tracks so we can extract unique albums.
        val url = "https://api.spotify.com/v1/me/player/recently-played?limit=50"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        client.newCall(request).enqueue(object: Callback {
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
                        // Use a map to group by album id for uniqueness.
                        val albumMap = mutableMapOf<String, Album>()
                        for (i in 0 until items.length()) {
                            val item = items.getJSONObject(i)
                            val track = item.getJSONObject("track")
                            val albumObj = track.getJSONObject("album")
                            val albumId = albumObj.getString("id")
                            if (!albumMap.containsKey(albumId)) {
                                val albumName = albumObj.getString("name")
                                val images = albumObj.optJSONArray("images")
                                val albumImageUrl = if (images != null && images.length() > 0)
                                    images.getJSONObject(0).getString("url")
                                else null
                                albumMap[albumId] = Album(albumId, albumName, albumImageUrl)
                            }
                        }
                        onSuccess(albumMap.values.toList())
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
}
