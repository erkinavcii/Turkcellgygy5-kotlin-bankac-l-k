package com.example.turkcellintro.data.repository

import com.example.turkcellintro.data.model.Book
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import com.example.turkcellintro.data.model.BorrowRecord
import com.example.turkcellintro.data.remote.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

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

    // KİTAP ÖDÜNÇ AL (YENİ)
    suspend fun borrowBook(book: Book) = withContext(Dispatchers.IO) {
        val user = SupabaseClient.client.auth.currentUserOrNull() ?: return@withContext
        
        // 1. Ödünç kaydı oluştur (Maks 5 gün)
        val dueDate = LocalDate.now().plusDays(5).toString()
        val record = BorrowRecord(
            user_id = user.id,
            book_id = book.id ?: "",
            due_date = dueDate
        )
        postgrest["borrow_records"].insert(record)

        // 2. Kitabın stok sayısını 1 azalt
        val newAvailable = (book.available_copies - 1).coerceAtLeast(0)
        postgrest["books"].update({
            set("available_copies", newAvailable)
        }) {
            filter { eq("id", book.id ?: "") }
        }
    }

    // KULLANICININ KİRALAMALARINI GETİR (YENİ)
    suspend fun getMyBorrows(): List<BorrowRecord> = withContext(Dispatchers.IO) {
        val user = SupabaseClient.client.auth.currentUserOrNull() ?: return@withContext emptyList()
        postgrest["borrow_records"].select {
            filter { eq("user_id", user.id) }
        }.decodeList<BorrowRecord>()
    }
}
