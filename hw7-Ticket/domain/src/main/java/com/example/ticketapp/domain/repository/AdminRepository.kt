package com.example.ticketapp.domain.repository

import com.example.ticketapp.domain.model.Event

interface AdminRepository {
    suspend fun createEvent(
        name: String,
        description: String?,
        place: String,
        startsAt: String,
        endsAt: String
    ): Result<Event>

    suspend fun updateEvent(
        id: String,
        name: String?,
        description: String?,
        place: String?,
        startsAt: String?,
        endsAt: String?
    ): Result<Event>

    suspend fun deleteEvent(id: String): Result<Unit>
}
