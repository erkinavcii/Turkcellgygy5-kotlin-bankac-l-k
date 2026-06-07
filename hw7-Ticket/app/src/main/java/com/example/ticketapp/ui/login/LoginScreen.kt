package com.example.ticketapp.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToDashboard: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state = viewModel.uiState

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bilet Uygulamasına Hoş Geldiniz", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-posta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Şifre") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (state is LoginUiState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Giriş Yap")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(onClick = onNavigateToRegister) {
                Text("Hesabın yok mu? Kayıt Ol")
            }
        }

        // Rol tabanlı yönlendirme
        LaunchedEffect(state) {
            when (state) {
                is LoginUiState.Success -> {
                    val role = state.data.user.userRole
                    val greeting = state.data.user.email
                    when (role) {
                        com.example.ticketapp.domain.model.UserRole.ADMIN,
                        com.example.ticketapp.domain.model.UserRole.STAFF -> {
                            Toast.makeText(context, "Yönetici Paneline Hoş Geldin! ($greeting)", Toast.LENGTH_LONG).show()
                            onNavigateToAdmin()
                        }
                        com.example.ticketapp.domain.model.UserRole.USER -> {
                            Toast.makeText(context, "Giriş Başarılı! Hoş geldin $greeting", Toast.LENGTH_LONG).show()
                            onNavigateToDashboard()
                        }
                    }
                }
                is LoginUiState.Error -> {
                    Toast.makeText(context, "Hata: ${state.message}", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }
}
