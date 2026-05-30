package com.example.ticketapp.core

import com.example.ticketapp.domain.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TokenAuthenticator : Authenticator, KoinComponent {

    private val sessionManager: SessionManager by inject()
    
    // Lazily inject AuthRepository interface from domain to avoid direct data module coupling
    private val authRepository: AuthRepository by inject()

    override fun authenticate(route: Route?, response: Response): Request? {
        // If we don't have a session or refresh token, we can't authenticate
        val refreshToken = sessionManager.session?.refreshToken ?: return null

        synchronized(this) {
            val currentToken = sessionManager.token
            val requestToken = response.request.header("Authorization")?.replace("Bearer ", "")

            // If the token was already refreshed by another concurrent request, retry with the new token
            if (requestToken != currentToken && currentToken != null) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            // Run blocking coroutine to refresh the token synchronously
            val refreshResult = runBlocking {
                authRepository.refreshToken(refreshToken)
            }

            return if (refreshResult.isSuccess) {
                val newAccessToken = refreshResult.getOrNull()?.accessToken
                if (newAccessToken != null) {
                    response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                } else {
                    sessionManager.clearSession()
                    null
                }
            } else {
                // Refresh failed or revoked: clear the session so that session flows automatically push user to Login Screen
                sessionManager.clearSession()
                null
            }
        }
    }
}
