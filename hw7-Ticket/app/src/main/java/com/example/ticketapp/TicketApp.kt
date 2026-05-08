package com.example.ticketapp

import android.app.Application
import com.example.ticketapp.core.di.coreModule
import com.example.ticketapp.data.di.dataModule
import com.example.ticketapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TicketApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Koin'i Başlat
        startKoin {
            androidLogger() // Koin loglarını gör
            androidContext(this@TicketApp) // Context sağla
            modules(listOf(coreModule, dataModule, appModule)) // Tüm modülleri yükle
        }
    }
}
