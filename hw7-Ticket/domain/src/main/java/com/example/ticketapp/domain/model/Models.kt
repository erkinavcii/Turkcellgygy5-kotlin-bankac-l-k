package com.example.ticketapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// API'den gelen rol değerleri: "USER", "STAFF", "ADMIN"
enum class UserRole {
    USER, STAFF, ADMIN;

    companion object {
        fun from(value: String?): UserRole = when (value?.uppercase()) {
            "ADMIN" -> ADMIN
            "STAFF" -> STAFF
            else -> USER
        }
    }
}

@Serializable
data class User(
    val id: String,
    val email: String,
    val role: String? = null, // "USER", "STAFF", "ADMIN"
    val firstName: String? = null,
    val lastName: String? = null
) {
    // Kolay erişim için computed property
    val userRole: UserRole get() = UserRole.from(role)
}

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)

@Serializable
data class TicketType(
    val id: String,
    val name: String,
    val priceCents: Int,
    val capacity: Int,
    val soldCount: Int,
    val remaining: Int
)

@Serializable
data class Event(
    val id: String,
    val name: String,
    val description: String? = null,
    val venue: String,
    val startsAt: String,
    val endsAt: String,
    val ticketTypes: List<TicketType> = emptyList()
)

@Serializable
data class Ticket(
    val id: String,
    val qrCode: String,
    val status: String, // "VALID" veya "USED"
    val ticketTypeId: String
)

@Serializable
data class PurchaseItem(
    val id: String,
    val ticketTypeId: String,
    val quantity: Int,
    val unitPriceCents: Int
)

@Serializable
data class Purchase(
    val id: String,
    val status: String, // "PENDING", "PAID", "FAILED"
    val totalCents: Int,
    val paidAt: String? = null,
    val items: List<PurchaseItem> = emptyList(),
    val tickets: List<Ticket> = emptyList()
)

@Serializable
data class PurchaseItemCreate(
    val ticketTypeId: String,
    val quantity: Int
)

@Serializable
data class PurchaseCreate(
    val items: List<PurchaseItemCreate>
)
