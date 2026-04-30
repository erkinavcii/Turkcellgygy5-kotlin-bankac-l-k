package com.example.turkcellintro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.turkcellintro.data.remote.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ÖDEV 1: KAYIT OL SUCCESS YAPISI & GİRİŞ YAPMA
 */
sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Success : AuthState() 
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    // KAYIT OL (ÖDEV 1)
    fun signUp(emailInput: String, passInput: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                SupabaseClient.client.auth.signUpWith(Email) {
                    email = emailInput
                    password = passInput
                }
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Kayıt başarısız")
            }
        }
    }

    // GİRİŞ YAP
    fun login(emailInput: String, passInput: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                SupabaseClient.client.auth.signInWith(Email) {
                    email = emailInput
                    password = passInput
                }
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Giriş başarısız")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                SupabaseClient.client.auth.signOut()
                _authState.value = AuthState.Initial
            } catch (e: Exception) {
                // Hata olsa bile ana ekrana dön
                _authState.value = AuthState.Initial
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Initial
    }
}
