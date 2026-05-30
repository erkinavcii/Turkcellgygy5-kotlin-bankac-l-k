package com.example.ticketapp.data.repository

import com.example.ticketapp.data.remote.AuthService
import com.example.ticketapp.domain.model.AuthResponse
import com.example.ticketapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val sessionManager: com.example.ticketapp.core.SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authService.login(mapOf("email" to email, "password" to password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                sessionManager.saveSession(authResponse)
                Result.success(authResponse)
            } else {
                val errorBody = response.errorBody()?.string() ?: ""
                Result.failure(Exception("Giriş başarısız: ${response.code()} $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val request = mapOf(
                "firstName" to firstName,
                "lastName" to lastName,
                "email" to email,
                "password" to password
            )
            val response = authService.register(request)
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                sessionManager.saveSession(authResponse)
                Result.success(authResponse)
            } else {
                val errorBody = response.errorBody()?.string() ?: ""
                val errorMsg = if (response.code() == 409 || errorBody.contains("email_taken")) {
                    "email_taken"
                } else {
                    "${response.code()} $errorBody"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = authService.logout()
            sessionManager.clearSession()
            if (response.isSuccessful) Result.success(Unit) else Result.failure(Exception("Çıkış hatası"))
        } catch (e: Exception) {
            sessionManager.clearSession()
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authService.refreshToken(mapOf("refreshToken" to refreshToken))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                sessionManager.saveSession(authResponse)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Token yenileme hatası"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
