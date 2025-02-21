package com.example.socialmelody.auth

object AuthConfig {
    const val CLIENT_ID = "99cf4c7cef3a4f239c43d82873b0ad7b"
    const val REDIRECT_URI = "socialmelody://callback"
    const val AUTH_ENDPOINT = "https://accounts.spotify.com/authorize"
    const val TOKEN_ENDPOINT = "https://accounts.spotify.com/api/token"
    val SCOPES = listOf(
        "user-read-private",
        "user-read-email",
        "user-read-playback-state",
        "user-modify-playback-state",
        "playlist-read-private",
        "playlist-modify-private",
        "user-read-recently-played"
    ).joinToString(" ")
}