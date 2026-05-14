package com.example.ticketapp.di

import com.example.ticketapp.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    
    // LoginViewModel'ı Koin'e kaydet (Repository'yi otomatik enjekte eder)
    viewModel {
        LoginViewModel(get())
    }

    viewModel {
        RegisterViewModel(get())
    }
}
