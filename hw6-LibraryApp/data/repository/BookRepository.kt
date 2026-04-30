package com.example.turkcellintro.data.repository

import com.example.turkcellintro.data.model.Book
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ÖDEV 2: BOOK REPOSITORY GÜNCELLEME, SİLME, ARAMA FONKSİYONLARI
 */
class BookRepository(private val postgrest: Postgrest) {

    // 1. Kitap Ekleme Fonksiyonu
    suspend fun addBook(book: Book) = withContext(Dispatchers.IO) {
        postgrest["books"].insert(book)
    }

    // 2. Arama Fonksiyonu (Başlığa göre filtreleme)
    suspend fun searchBooks(query: String): List<Book> = withContext(Dispatchers.IO) {
        return@withContext postgrest["books"]
            .select(columns = Columns.ALL) {
                filter {
                    ilike("title", "%$query%") // Başlıkta geçen kelimeye göre arama
                }
            }
            .decodeList<Book>()
    }

    // 2. Güncelleme Fonksiyonu
    suspend fun updateBook(book: Book) = withContext(Dispatchers.IO) {
        postgrest["books"].update(book) {
            filter {
                eq("id", book.id ?: "")
            }
        }
    }

    // 3. Silme Fonksiyonu
    suspend fun deleteBook(bookId: String) = withContext(Dispatchers.IO) {
        postgrest["books"].delete {
            filter {
                eq("id", bookId)
            }
        }
    }

    // Ek: Tüm kitapları getir
    suspend fun getAllBooks(): List<Book> = withContext(Dispatchers.IO) {
        return@withContext postgrest["books"].select().decodeList<Book>()
    }
}
