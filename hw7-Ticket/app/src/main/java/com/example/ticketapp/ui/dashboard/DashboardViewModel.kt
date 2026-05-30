package com.example.ticketapp.ui.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketapp.core.util.toUserMessage
import com.example.ticketapp.domain.model.Event
import com.example.ticketapp.domain.model.Purchase
import com.example.ticketapp.domain.model.Ticket
import com.example.ticketapp.domain.repository.EventRepository
import com.example.ticketapp.domain.repository.TicketRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

sealed interface EventsState {
    object Loading : EventsState
    data class Success(val events: List<Event>) : EventsState
    data class Error(val message: String) : EventsState
}

sealed interface TicketsState {
    object Loading : TicketsState
    data class Success(val tickets: List<Ticket>) : TicketsState
    data class Error(val message: String) : TicketsState
}

sealed interface CheckoutState {
    object Idle : CheckoutState
    object Processing : CheckoutState
    data class Success(val purchase: Purchase) : CheckoutState
    data class Error(val message: String) : CheckoutState
}

class DashboardViewModel(
    private val eventRepository: EventRepository,
    private val ticketRepository: TicketRepository,
    val sessionManager: com.example.ticketapp.core.SessionManager
) : ViewModel() {

    var selectedTab by mutableStateOf(0)

    var eventsState by mutableStateOf<EventsState>(EventsState.Loading)
        private set

    var ticketsState by mutableStateOf<TicketsState>(TicketsState.Loading)
        private set

    var checkoutState by mutableStateOf<CheckoutState>(CheckoutState.Idle)
        private set

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            eventsState = EventsState.Loading
            ticketsState = TicketsState.Loading
            
            val eventsDeferred = async { eventRepository.getEvents() }
            val ticketsDeferred = async { ticketRepository.getMyTickets() }

            val eventsResult = eventsDeferred.await()
            val ticketsResult = ticketsDeferred.await()

            eventsState = eventsResult.fold(
                onSuccess = { EventsState.Success(it) },
                onFailure = { EventsState.Error(it.toUserMessage()) }
            )

            ticketsState = ticketsResult.fold(
                onSuccess = { TicketsState.Success(it) },
                onFailure = { TicketsState.Error(it.toUserMessage()) }
            )
        }
    }

    fun purchaseTicket(ticketTypeId: String, quantity: Int) {
        viewModelScope.launch {
            checkoutState = CheckoutState.Processing
            val result = ticketRepository.purchaseTickets(ticketTypeId, quantity)
            checkoutState = result.fold(
                onSuccess = { purchase ->
                    // Biletleri ve etkinlikleri tekrar yükleyerek arayüzü güncelle
                    loadDashboardData()
                    CheckoutState.Success(purchase)
                },
                onFailure = { CheckoutState.Error(it.toUserMessage()) }
            )
        }
    }

    fun resetCheckoutState() {
        checkoutState = CheckoutState.Idle
    }
}
