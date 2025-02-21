package com.example.socialmelody.utils

import android.content.Context

fun getAccessToken(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("access_token", null)
}
