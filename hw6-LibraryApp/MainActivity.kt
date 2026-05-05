package com.example.turkcellintro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.turkcellintro.data.remote.SupabaseClient
import com.example.turkcellintro.data.repository.BookRepository
import com.example.turkcellintro.ui.screen.BookListScreen
import com.example.turkcellintro.ui.screen.LoginScreen
import com.example.turkcellintro.ui.screen.RegisterScreen
import com.example.turkcellintro.ui.screen.MyBorrowsScreen
import io.github.jan.supabase.postgrest.postgrest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Repository'yi Supabase client ile başlatıyoruz
        val bookRepository = BookRepository(SupabaseClient.client.postgrest)
        
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    
                    NavHost(navController = navController, startDestination = "login") {
                        // 1. Giriş Ekranı
                        composable("login") {
                            LoginScreen(
                                onNavigateToRegister = { navController.navigate("register") },
                                onNavigateToHome = { navController.navigate("home") }
                            )
                        }
                        
                        // 2. Kayıt Ekranı
                        composable("register") {
                            RegisterScreen(
                                onNavigateToHome = { navController.navigate("home") }
                            )
                        }
                        
                        // 3. Ana Liste Ekranı
                        composable("home") {
                            BookListScreen(
                                repository = bookRepository,
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                onNavigateToMyBorrows = { navController.navigate("my_borrows") }
                            )
                        }

                        // 4. Kiralamalarım Ekranı
                        composable("my_borrows") {
                            MyBorrowsScreen(
                                repository = bookRepository,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
