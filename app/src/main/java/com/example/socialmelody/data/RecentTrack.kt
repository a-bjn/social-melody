package com.example.socialmelody.data

data class RecentTrack(
    val trackId: String,
    val trackName: String,
    val albumName: String,
    val albumImageUrl: String?,
    val playedAt: String
)