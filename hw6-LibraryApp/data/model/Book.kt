package com.example.turkcellintro.data.model

import kotlinx.serialization.Serializable

/**
 * ÖDEV 3: KİTAP MODELİ
 * Supabase/Postgrest ile uyumlu olması için @Serializable eklenmiştir.
 */
@Serializable
data class Book(
    val id: String? = null,
    val title: String = "",
    val author: String = "",
    val isbn: String = "",
    val category: String = "",
    val page_count: Int = 0,
    val total_copies: Int = 1,
    val available_copies: Int = 1
)
