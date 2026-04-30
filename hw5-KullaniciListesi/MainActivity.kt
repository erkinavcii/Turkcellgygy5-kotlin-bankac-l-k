package com.example.userapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.userapp.data.remote.RetrofitInstance
import com.example.userapp.data.repository.UserRepository
import com.example.userapp.ui.screen.UserListScreen
import com.example.userapp.viewmodel.UserViewModel
import com.example.userapp.viewmodel.UserViewModelFactory // Eğer Factory gerekirse diye eklenebilir

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Repository Manuel Entegrasyon (Hilt yoksa bu şekilde bağlanır)
        val repository = UserRepository(RetrofitInstance.api)
        
        setContent {
            // Basit bir ViewModel başlatma (Factory olmadan kullanım için)
            val viewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                        return UserViewModel(repository) as T
                    }
                }
            )
            
            UserListScreen(viewModel = viewModel)
        }
    }
}
