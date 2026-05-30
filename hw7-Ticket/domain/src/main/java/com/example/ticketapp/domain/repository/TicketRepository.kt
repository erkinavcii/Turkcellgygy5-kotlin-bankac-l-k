package com.example.ticketapp.domain.repository

import com.example.ticketapp.domain.model.Purchase
import com.example.ticketapp.domain.model.Ticket

interface TicketRepository {
    suspend fun getMyTickets(): Result<List<Ticket>>
    suspend fun getMyPurchases(): Result<List<Purchase>>
    suspend fun purchaseTickets(ticketTypeId: String, quantity: Int): Result<Purchase>
}
