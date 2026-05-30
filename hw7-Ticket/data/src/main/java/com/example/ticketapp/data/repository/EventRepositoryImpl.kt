package com.example.ticketapp.data.repository

import com.example.ticketapp.data.remote.TicketApiService
import com.example.ticketapp.domain.model.Event
import com.example.ticketapp.domain.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepositoryImpl(
    private val ticketApiService: TicketApiService
) : EventRepository {
    
    override suspend fun getEvents(upcoming: Boolean?): Result<List<Event>> = withContext(Dispatchers.IO) {
        try {
            val upcomingStr = upcoming?.toString()
            val response = ticketApiService.getEvents(upcomingStr)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Etkinlik listesi yüklenemedi: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEventDetails(id: String): Result<Event> = withContext(Dispatchers.IO) {
        try {
            val response = ticketApiService.getEventDetails(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Etkinlik detayı yüklenemedi: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
