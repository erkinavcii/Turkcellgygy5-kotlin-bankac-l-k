package com.example.ticketapp.data.remote

import com.example.ticketapp.domain.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body request: Map<String, String>): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: Map<String, String>): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(): Response<Unit>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: Map<String, String>): Response<AuthResponse>
}
