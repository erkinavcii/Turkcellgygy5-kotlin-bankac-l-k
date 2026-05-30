package com.example.ticketapp.ui.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketapp.core.util.toUserMessage
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

    private val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

    fun register(firstName: String, lastName: String, email: String, password: String) {
        if (firstName.isBlank() || lastName.isBlank()) {
            uiState = RegisterUiState.Error("Ad ve Soyad alanları boş bırakılamaz.")
            return
        }
        if (!emailPattern.matches(email)) {
            uiState = RegisterUiState.Error("Lütfen geçerli bir e-posta adresi girin.")
            return
        }
        if (password.length !in 8..128) {
            uiState = RegisterUiState.Error("Şifre 8 ile 128 karakter arasında olmalıdır.")
            return
        }

        viewModelScope.launch {
            uiState = RegisterUiState.Loading
            val result = authRepository.register(firstName, lastName, email, password)
            uiState = result.fold(
                onSuccess = { RegisterUiState.Success(it) },
                onFailure = { RegisterUiState.Error(it.toUserMessage()) }
            )
        }
    }
}
