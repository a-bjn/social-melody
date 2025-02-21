package com.example.socialmelody.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val SpotifyDarkColors = darkColorScheme(
    primary = SpotifyGreen,
    onPrimary = DarkBackground,
    background = DarkBackground,
    onBackground = LightText,
    surface = LightDarkBackground,
    onSurface = LightText,
)


@Composable
fun SocialMelodyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> SpotifyDarkColors
        else -> SpotifyDarkColors
    }

    MaterialTheme(
        colorScheme = SpotifyDarkColors,
        typography = Typography,
        content = content
    )
}