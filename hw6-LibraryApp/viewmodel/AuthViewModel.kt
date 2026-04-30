package com.example.libraryapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ÖDEV 1: KAYIT OL SUCCESS YAPISI
 */
sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Success : AuthState() // Başarılı kayıt durumu
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    fun signUp(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // Burada Supabase Auth çağrısı yapılır (Örn: supabase.auth.signUpWith(Email) )
                // Simülasyon için:
                kotlinx.coroutines.delay(1500)
                
                // İşlem başarılı ise Success durumuna geçilir
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Kayıt başarısız")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Initial
    }
}
