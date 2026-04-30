package com.example.libraryapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.libraryapp.data.model.Book
import com.example.libraryapp.data.repository.BookRepository
import com.example.libraryapp.ui.components.BookCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(repository: BookRepository) {
    val scope = rememberCoroutineScope()
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // Sayfa açıldığında verileri çek
    LaunchedEffect(Unit) {
        books = repository.getAllBooks()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kütüphanem") },
                actions = {
                    IconButton(onClick = { /* Arama tetiklenebilir */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Ara")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Yeni kitap ekleme sayfası açılabilir */ }) {
                Icon(Icons.Default.Add, contentDescription = "Ekle")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(books) { book ->
                        BookCard(
                            book = book,
                            onDeleteClick = {
                                scope.launch {
                                    repository.deleteBook(book.id ?: "")
                                    books = repository.getAllBooks() // Listeyi tazele
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
