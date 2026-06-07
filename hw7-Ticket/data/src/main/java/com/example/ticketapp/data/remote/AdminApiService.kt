package com.example.ticketapp.data.remote

import com.example.ticketapp.domain.model.Event
import retrofit2.Response
import retrofit2.http.*

interface AdminApiService {

    @POST("admin/events")
    suspend fun createEvent(
        @Body body: Map<String, String?>
    ): Response<Event>

    @PATCH("admin/events/{id}")
    suspend fun updateEvent(
        @Path("id") id: String,
        @Body body: Map<String, String?>
    ): Response<Event>

    @DELETE("admin/events/{id}")
    suspend fun deleteEvent(
        @Path("id") id: String
    ): Response<Unit>
}
