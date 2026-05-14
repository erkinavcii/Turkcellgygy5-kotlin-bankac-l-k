package com.example.ticketapp.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketapp.domain.model.AuthResponse
import com.example.ticketapp.domain.repository.AuthRepository
import kotlinx.coroutines.launch

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val data: AuthResponse) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
        private set

    fun register(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            uiState = RegisterUiState.Loading
            val result = authRepository.register(firstName, lastName, email, password)
            uiState = result.fold(
                onSuccess = { RegisterUiState.Success(it) },
                onFailure = { RegisterUiState.Error(it.message ?: "Kayıt sırasında bir hata oluştu") }
            )
        }
    }
}
