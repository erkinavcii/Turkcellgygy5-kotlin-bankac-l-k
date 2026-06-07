package com.example.ticketapp.di

import com.example.ticketapp.ui.admin.AdminDashboardViewModel
import com.example.ticketapp.ui.dashboard.DashboardViewModel
import com.example.ticketapp.ui.login.LoginViewModel
import com.example.ticketapp.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    
    // LoginViewModel'ı Koin'e kaydet (Repository'yi otomatik enjekte eder)
    viewModel {
        LoginViewModel(get())
    }

    // RegisterViewModel'ı Koin'e kaydet
    viewModel {
        RegisterViewModel(get())
    }

    // DashboardViewModel'ı Koin'e kaydet (EventRepository, TicketRepository, SessionManager)
    viewModel {
        DashboardViewModel(get(), get(), get())
    }

    // AdminDashboardViewModel'ı Koin'e kaydet (EventRepository, AdminRepository, SessionManager)
    viewModel {
        AdminDashboardViewModel(get(), get(), get())
    }
}
