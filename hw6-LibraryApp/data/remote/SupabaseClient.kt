package com.example.libraryapp.data.remote

import io.github.jan_tennert.supabase.createSupabaseClient
import io.github.jan_tennert.supabase.gotrue.Auth
import io.github.jan_tennert.supabase.postgrest.Postgrest

/**
 * GERÇEK SUPABASE BAĞLANTISI
 * Bu nesne uygulama genelinde tek bir bağlantı kurulmasını sağlar.
 */
object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = SupabaseConfig.SUPABASE_URL,
        supabaseKey = SupabaseConfig.SUPABASE_ANON_KEY
    ) {
        install(Postgrest) // Veritabanı işlemleri için
        install(Auth)      // Giriş/Kayıt işlemleri için
    }
}
