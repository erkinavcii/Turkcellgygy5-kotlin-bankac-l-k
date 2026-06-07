package com.example.ticketapp.ui.admin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketapp.core.SessionManager
import com.example.ticketapp.core.util.toUserMessage
import com.example.ticketapp.domain.model.Event
import com.example.ticketapp.domain.model.UserRole
import com.example.ticketapp.domain.repository.AdminRepository
import com.example.ticketapp.domain.repository.EventRepository
import kotlinx.coroutines.launch

// ---- UI States ----

sealed class AdminEventsState {
    object Loading : AdminEventsState()
    data class Success(val events: List<Event>) : AdminEventsState()
    data class Error(val message: String) : AdminEventsState()
}

sealed class AdminOperationState {
    object Idle : AdminOperationState()
    object Loading : AdminOperationState()
    data class Success(val message: String) : AdminOperationState()
    data class Error(val message: String) : AdminOperationState()
}

// ---- ViewModel ----

class AdminDashboardViewModel(
    private val eventRepository: EventRepository,
    private val adminRepository: AdminRepository,
    val sessionManager: SessionManager
) : ViewModel() {

    var eventsState by mutableStateOf<AdminEventsState>(AdminEventsState.Loading)
        private set

    var operationState by mutableStateOf<AdminOperationState>(AdminOperationState.Idle)
        private set

    // Dialog state
    var showDialog by mutableStateOf(false)
        private set

    var editingEvent by mutableStateOf<Event?>(null)
        private set

    // Derived admin info
    val adminEmail: String get() = sessionManager.session?.user?.email ?: "Yönetici"
    val adminRole: String get() = when (sessionManager.session?.user?.userRole) {
        UserRole.ADMIN -> "ADMIN"
        UserRole.STAFF -> "STAFF"
        else -> "USER"
    }

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            eventsState = AdminEventsState.Loading
            val result = eventRepository.getEvents()
            eventsState = result.fold(
                onSuccess = { AdminEventsState.Success(it) },
                onFailure = { AdminEventsState.Error(it.toUserMessage()) }
            )
        }
    }

    fun createEvent(name: String, description: String, place: String, startsAt: String, endsAt: String) {
        viewModelScope.launch {
            operationState = AdminOperationState.Loading
            val result = adminRepository.createEvent(name, description.ifBlank { null }, place, startsAt, endsAt)
            operationState = result.fold(
                onSuccess = {
                    loadEvents() // Listeyi yenile
                    AdminOperationState.Success("\"$name\" etkinliği başarıyla oluşturuldu.")
                },
                onFailure = { AdminOperationState.Error(it.toUserMessage()) }
            )
        }
    }

    fun updateEvent(id: String, name: String, description: String, place: String, startsAt: String, endsAt: String) {
        viewModelScope.launch {
            operationState = AdminOperationState.Loading
            val result = adminRepository.updateEvent(id, name, description.ifBlank { null }, place, startsAt, endsAt)
            operationState = result.fold(
                onSuccess = {
                    loadEvents() // Listeyi yenile
                    AdminOperationState.Success("Etkinlik başarıyla güncellendi.")
                },
                onFailure = { AdminOperationState.Error(it.toUserMessage()) }
            )
        }
    }

    fun deleteEvent(id: String) {
        viewModelScope.launch {
            operationState = AdminOperationState.Loading
            val result = adminRepository.deleteEvent(id)
            operationState = result.fold(
                onSuccess = {
                    loadEvents() // Listeyi yenile
                    AdminOperationState.Success("Etkinlik başarıyla silindi.")
                },
                onFailure = { AdminOperationState.Error(it.toUserMessage()) }
            )
        }
    }

    fun showCreateDialog() {
        editingEvent = null
        showDialog = true
    }

    fun showEditDialog(event: Event) {
        editingEvent = event
        showDialog = true
    }

    fun hideDialog() {
        showDialog = false
        editingEvent = null
    }

    fun resetOperationState() {
        operationState = AdminOperationState.Idle
    }
}
