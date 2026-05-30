package com.example.ticketapp.data.remote

import com.example.ticketapp.domain.model.Event
import com.example.ticketapp.domain.model.Purchase
import com.example.ticketapp.domain.model.PurchaseCreate
import com.example.ticketapp.domain.model.Ticket
import retrofit2.Response
import retrofit2.http.*

interface TicketApiService {
    
    @GET("events")
    suspend fun getEvents(
        @Query("upcoming") upcoming: String? = null
    ): Response<List<Event>>

    @GET("events/{id}")
    suspend fun getEventDetails(
        @Path("id") id: String
    ): Response<Event>

    @POST("purchases")
    suspend fun createPurchase(
        @Body request: PurchaseCreate
    ): Response<Purchase>

    @POST("purchases/{id}/pay")
    suspend fun payPurchase(
        @Path("id") id: String
    ): Response<Purchase>

    @GET("me/purchases")
    suspend fun getMyPurchases(): Response<List<Purchase>>

    @GET("me/tickets")
    suspend fun getMyTickets(): Response<List<Ticket>>
}
