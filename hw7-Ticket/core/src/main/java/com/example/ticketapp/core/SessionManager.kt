package com.example.ticketapp.core

import com.example.ticketapp.domain.model.AuthResponse

class SessionManager {
    @Volatile
    var session: AuthResponse? = null
        private set

    val isLoggedIn: Boolean
        get() = session != null

    val token: String?
        get() = session?.accessToken

    fun saveSession(newSession: AuthResponse) {
        synchronized(this) {
            session = newSession
        }
    }

    fun clearSession() {
        synchronized(this) {
            session = null
        }
    }
}
