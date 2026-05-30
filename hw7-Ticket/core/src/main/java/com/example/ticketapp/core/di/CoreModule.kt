package com.example.ticketapp.core.di

import com.example.ticketapp.core.SessionManager
import com.example.ticketapp.core.TokenAuthenticator
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit

val coreModule = module {
    
    // SessionManager'ı Koin'e kaydet
    single { SessionManager() }
    
    // TokenAuthenticator'ı Koin'e kaydet
    single { TokenAuthenticator() }
    
    // JSON Yapılandırması
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    // OkHttp (Logging, Auth Interceptor ve Token Authenticator ile)
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val sessionManager: SessionManager = get()
        
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                sessionManager.token?.let { token ->
                    requestBuilder.header("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .authenticator(get<TokenAuthenticator>())
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
