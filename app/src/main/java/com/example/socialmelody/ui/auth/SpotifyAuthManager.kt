package com.example.socialmelody.ui.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.example.socialmelody.ui.navigation.Screen
import net.openid.appauth.*

class SpotifyAuthManager(private val context: Context) {
    private val authService = AuthorizationService(context)

    fun startLogin(authLauncher: (Intent) -> Unit) {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse(AuthConfig.AUTH_ENDPOINT),
            Uri.parse(AuthConfig.TOKEN_ENDPOINT)
        )

        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            AuthConfig.CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(AuthConfig.REDIRECT_URI)
        )
            .setScopes(AuthConfig.SCOPES)
            .setCodeVerifier(CodeVerifierUtil.generateRandomCodeVerifier())
            .build()

        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        authLauncher(authIntent)
    }

    fun handleAuthResponse(intent: Intent?, navController: NavController) {
        if (intent == null) {
            Log.e("SpotifyAuth", "Intent is null, authentication failed")
            return
        }

        val authResponse = AuthorizationResponse.fromIntent(intent)
        val authException = AuthorizationException.fromIntent(intent)

        if (authResponse != null) {
            authService.performTokenRequest(
                authResponse.createTokenExchangeRequest()
            ) { response, ex ->
                if (response != null) {
                    val accessToken = response.accessToken.orEmpty()
                    storeAccessToken(context, accessToken)
                    Log.d("SpotifyAuth", "Access Token: $accessToken")

                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                } else {
                    Log.e("SpotifyAuth", "Auth error: ${ex?.message}")
                }
            }
        } else {
            Log.e("SpotifyAuth", "Authorization failed: ${authException?.message}")
        }
    }
}

fun storeAccessToken(context: Context, accessToken: String) {
    val sharedPreferences = context.getSharedPreferences("spotify_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("access_token", accessToken).apply()
}
