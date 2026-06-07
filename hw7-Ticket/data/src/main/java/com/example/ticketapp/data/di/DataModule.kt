package com.example.ticketapp.data.di

import com.example.ticketapp.data.remote.AdminApiService
import com.example.ticketapp.data.remote.AuthService
import com.example.ticketapp.data.remote.TicketApiService
import com.example.ticketapp.data.repository.AdminRepositoryImpl
import com.example.ticketapp.data.repository.AuthRepositoryImpl
import com.example.ticketapp.data.repository.EventRepositoryImpl
import com.example.ticketapp.data.repository.TicketRepositoryImpl
import com.example.ticketapp.domain.repository.AdminRepository
import com.example.ticketapp.domain.repository.AuthRepository
import com.example.ticketapp.domain.repository.EventRepository
import com.example.ticketapp.domain.repository.TicketRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val dataModule = module {
    
    // AuthService'i Retrofit üzerinden sağla
    single<AuthService> {
        get<Retrofit>().create(AuthService::class.java)
    }

    // TicketApiService'i Retrofit üzerinden sağla
    single<TicketApiService> {
        get<Retrofit>().create(TicketApiService::class.java)
    }

    // AuthRepository arayüzünü Implementation sınıfına bağla
    single<AuthRepository> {
        AuthRepositoryImpl(get(), get())
    }

    // EventRepository arayüzünü Implementation sınıfına bağla
    single<EventRepository> {
        EventRepositoryImpl(get())
    }

    // TicketRepository arayüzünü Implementation sınıfına bağla
    single<TicketRepository> {
        TicketRepositoryImpl(get())
    }

    // AdminApiService'i Retrofit üzerinden sağla
    single<AdminApiService> {
        get<Retrofit>().create(AdminApiService::class.java)
    }

    // AdminRepository arayüzünü Implementation sınıfına bağla
    single<AdminRepository> {
        AdminRepositoryImpl(get())
    }
}
