package com.example.ticketapp.domain.repository

import com.example.ticketapp.domain.model.AuthResponse
import com.example.ticketapp.domain.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun logout(): Result<Unit>
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse>
}
