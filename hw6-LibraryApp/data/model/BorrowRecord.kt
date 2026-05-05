package com.example.turkcellintro.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BorrowRecord(
    val id: String? = null,
    val user_id: String,
    val book_id: String,
    val borrow_date: String? = null,
    val due_date: String,
    val status: String = "active"
)

/**
 * Kiralamalar ekranında göstermek için kitap bilgisiyle birleştirilmiş model
 */
@Serializable
data class BorrowWithBook(
    val record: BorrowRecord,
    val book_title: String,
    val book_author: String
)
