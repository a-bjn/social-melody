package com.example.socialmelody.ui.api

import android.util.Log
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

fun playSong(accessToken: String, trackUri: String) {
    val url = "https://api.spotify.com/v1/me/player/play"

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

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("SpotifyPlayback", "Failed to send play command: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.d("SpotifyPlayback", "Playback started successfully!")
            } else {
                Log.e("SpotifyPlayback", "Error starting playback: ${response.body?.string()}")
            }
        }
    })
}