package com.example.ticketapp.data.repository

import com.example.ticketapp.data.remote.AuthService
import com.example.ticketapp.domain.model.AuthResponse
import com.example.ticketapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authService: AuthService
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authService.login(mapOf("email" to email, "password" to password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Giriş başarısız: ${response.message()}"))
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
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Kayıt başarısız: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = authService.logout()
            if (response.isSuccessful) Result.success(Unit) else Result.failure(Exception("Çıkış hatası"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authService.refreshToken(mapOf("refreshToken" to refreshToken))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Token yenileme hatası"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
