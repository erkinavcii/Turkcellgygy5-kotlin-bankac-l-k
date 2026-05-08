package com.example.ticketapp.data.di

import com.example.ticketapp.data.remote.AuthService
import com.example.ticketapp.data.repository.AuthRepositoryImpl
import com.example.ticketapp.domain.repository.AuthRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule = module {
    
    // AuthService'i Retrofit üzerinden sağla
    single<AuthService> {
        get<Retrofit>().create(AuthService::class.java)
    }

    // AuthRepository arayüzünü Implementation sınıfına bağla
    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }
}
