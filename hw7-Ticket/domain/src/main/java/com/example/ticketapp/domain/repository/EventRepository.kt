package com.example.ticketapp.domain.repository

import com.example.ticketapp.domain.model.Event

interface EventRepository {
    suspend fun getEvents(upcoming: Boolean? = null): Result<List<Event>>
    suspend fun getEventDetails(id: String): Result<Event>
}
