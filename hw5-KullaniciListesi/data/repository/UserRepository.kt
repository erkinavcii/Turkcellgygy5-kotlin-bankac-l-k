package com.example.userapp.data.repository

import com.example.userapp.data.model.User
import com.example.userapp.data.remote.ApiService

class UserRepository(private val apiService: ApiService) {
    suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
}
