package com.example.ticketapp.core.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit

val coreModule = module {
    
    // JSON Yapılandırması
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    // OkHttp (Logging Interceptor ile)
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    // Retrofit İstemcisi
    single {
        val baseUrl = "https://tickets-api.halitkalayci.com/"
        val json: Json = get()
        val contentType = "application/json".toMediaType()
        
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(get())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
