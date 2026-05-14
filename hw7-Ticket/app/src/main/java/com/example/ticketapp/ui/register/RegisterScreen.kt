package com.example.ticketapp.ui.register

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    val state = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Yeni Hesap Oluştur", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Ad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Soyad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

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

        Spacer(modifier = Modifier.height(24.dp))

        if (state is RegisterUiState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.register(firstName, lastName, email, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kayıt Ol")
            }
            
            TextButton(onClick = onNavigateToLogin) {
                Text("Zaten hesabın var mı? Giriş yap")
            }
        }

        // Durum takibi
        LaunchedEffect(state) {
            when (state) {
                is RegisterUiState.Success -> {
                    Toast.makeText(context, "Kayıt Başarılı! Hoş geldin ${state.data.user.firstName}", Toast.LENGTH_LONG).show()
                    // Opsiyonel: Başarılı kayıt sonrası login'e veya home'a yönlendirilebilir
                }
                is RegisterUiState.Error -> {
                    Toast.makeText(context, "Hata: ${state.message}", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }
}
