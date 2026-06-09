package com.example.ticketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ticketapp.core.SessionManager
import com.example.ticketapp.domain.model.UserRole
import com.example.ticketapp.ui.admin.AdminDashboardScreen
import com.example.ticketapp.ui.dashboard.DashboardScreen
import com.example.ticketapp.ui.login.LoginScreen
import com.example.ticketapp.ui.register.RegisterScreen
import com.example.ticketapp.ui.theme.Typography
import org.koin.android.ext.android.inject

enum class Screen {
    Login, Register, Dashboard, AdminDashboard
}

class MainActivity : ComponentActivity() {
    
    // Inject SessionManager to check login status
    private val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            // Premium Dark Theme Palette
            val darkColors = darkColorScheme(
                primary = Color(0xFF8A2BE2),      // Neon Violet
                secondary = Color(0xFF00F5FF),    // Electric Cyan
                background = Color(0xFF070B19),   // Obsidian Midnight Blue
                surface = Color(0xFF13182C),      // Dark Slate Blue Card
                onPrimary = Color.White,
                onSecondary = Color.Black,
                onBackground = Color.White,
                onSurface = Color.White
            )

            MaterialTheme(
                colorScheme = darkColors,
                typography = Typography
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Oturum açıksa role göre başlangıç ekranını belirle
                    var currentScreen by remember {
                        mutableStateOf(
                            when {
                                !sessionManager.isLoggedIn -> Screen.Login
                                sessionManager.session?.user?.userRole == UserRole.ADMIN ||
                                sessionManager.session?.user?.userRole == UserRole.STAFF -> Screen.AdminDashboard
                                else -> Screen.Dashboard
                            }
                        )
                    }

                    // Seamless state transition container
                    AnimatedContent(
                        targetState = currentScreen,
                        transitionSpec = {
                            fadeIn() with fadeOut()
                        },
                        label = "screenTransition"
                    ) { screen ->
                        when (screen) {
                            Screen.Login -> {
                                LoginScreen(
                                    onNavigateToDashboard = {
                                        currentScreen = Screen.Dashboard
                                    },
                                    onNavigateToAdmin = {
                                        currentScreen = Screen.AdminDashboard
                                    },
                                    onNavigateToRegister = {
                                        currentScreen = Screen.Register
                                    }
                                )
                            }
                            Screen.Register -> {
                                RegisterScreen(
                                    onNavigateToDashboard = {
                                        currentScreen = Screen.Dashboard
                                    },
                                    onNavigateToLogin = {
                                        currentScreen = Screen.Login
                                    }
                                )
                            }
                            Screen.Dashboard -> {
                                DashboardScreen(
                                    onLogout = {
                                        sessionManager.clearSession()
                                        currentScreen = Screen.Login
                                    }
                                )
                            }
                            Screen.AdminDashboard -> {
                                AdminDashboardScreen(
                                    onLogout = {
                                        sessionManager.clearSession()
                                        currentScreen = Screen.Login
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
