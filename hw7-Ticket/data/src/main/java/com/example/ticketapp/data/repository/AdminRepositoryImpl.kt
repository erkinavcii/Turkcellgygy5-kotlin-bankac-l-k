package com.example.ticketapp.data.repository

import com.example.ticketapp.data.remote.AdminApiService
import com.example.ticketapp.domain.model.Event
import com.example.ticketapp.domain.repository.AdminRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdminRepositoryImpl(
    private val adminApiService: AdminApiService
) : AdminRepository {

    override suspend fun createEvent(
        name: String,
        description: String?,
        place: String,
        startsAt: String,
        endsAt: String
    ): Result<Event> = withContext(Dispatchers.IO) {
        try {
            val body = buildMap<String, String?> {
                put("name", name)
                put("place", place)
                put("startsAt", startsAt)
                put("endsAt", endsAt)
                if (!description.isNullOrBlank()) put("description", description)
            }
            val response = adminApiService.createEvent(body)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string() ?: ""
                Result.failure(Exception("Etkinlik oluşturulamadı: ${response.code()} $error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateEvent(
        id: String,
        name: String?,
        description: String?,
        place: String?,
        startsAt: String?,
        endsAt: String?
    ): Result<Event> = withContext(Dispatchers.IO) {
        try {
            val body = buildMap<String, String?> {
                if (!name.isNullOrBlank()) put("name", name)
                if (!description.isNullOrBlank()) put("description", description)
                if (!place.isNullOrBlank()) put("place", place)
                if (!startsAt.isNullOrBlank()) put("startsAt", startsAt)
                if (!endsAt.isNullOrBlank()) put("endsAt", endsAt)
            }
            val response = adminApiService.updateEvent(id, body)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val error = response.errorBody()?.string() ?: ""
                Result.failure(Exception("Etkinlik güncellenemedi: ${response.code()} $error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteEvent(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = adminApiService.deleteEvent(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val error = response.errorBody()?.string() ?: ""
                Result.failure(Exception("Etkinlik silinemedi: ${response.code()} $error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
