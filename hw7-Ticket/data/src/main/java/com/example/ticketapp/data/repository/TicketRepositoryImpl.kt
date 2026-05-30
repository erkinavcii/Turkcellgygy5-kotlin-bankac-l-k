package com.example.ticketapp.data.repository

import com.example.ticketapp.data.remote.TicketApiService
import com.example.ticketapp.domain.model.Purchase
import com.example.ticketapp.domain.model.PurchaseCreate
import com.example.ticketapp.domain.model.PurchaseItemCreate
import com.example.ticketapp.domain.model.Ticket
import com.example.ticketapp.domain.repository.TicketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TicketRepositoryImpl(
    private val ticketApiService: TicketApiService
) : TicketRepository {

    override suspend fun getMyTickets(): Result<List<Ticket>> = withContext(Dispatchers.IO) {
        try {
            val response = ticketApiService.getMyTickets()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Biletleriniz alınamadı: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyPurchases(): Result<List<Purchase>> = withContext(Dispatchers.IO) {
        try {
            val response = ticketApiService.getMyPurchases()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Satın alımlarınız alınamadı: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun purchaseTickets(ticketTypeId: String, quantity: Int): Result<Purchase> = withContext(Dispatchers.IO) {
        try {
            // 1. Rezervasyon oluştur (PENDING satın alma)
            val createRequest = PurchaseCreate(
                items = listOf(PurchaseItemCreate(ticketTypeId = ticketTypeId, quantity = quantity))
            )
            val createResponse = ticketApiService.createPurchase(createRequest)
            if (!createResponse.isSuccessful || createResponse.body() == null) {
                return@withContext Result.failure(
                    Exception("Rezervasyon oluşturulamadı: ${createResponse.message()}")
                )
            }
            val pendingPurchase = createResponse.body()!!

            // 2. Ödemeyi otomatik yap (Simüle edilmiş ödeme endpoint'i)
            val payResponse = ticketApiService.payPurchase(pendingPurchase.id)
            if (payResponse.isSuccessful && payResponse.body() != null) {
                Result.success(payResponse.body()!!)
            } else {
                Result.failure(Exception("Ödeme işlemi başarısız: ${payResponse.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
