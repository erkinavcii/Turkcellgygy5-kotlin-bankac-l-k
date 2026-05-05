package com.example.turkcellintro.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.turkcellintro.data.model.Book
import com.example.turkcellintro.data.repository.BookRepository
import com.example.turkcellintro.ui.components.BookCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    repository: BookRepository,
    onLogout: () -> Unit,
    onNavigateToMyBorrows: () -> Unit // Yeni navigasyon
) {
    val scope = rememberCoroutineScope()
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // ... (mevcut diyalog durumları) ...
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf<Book?>(null) }

    // Sayfa açıldığında verileri çek
    LaunchedEffect(Unit) {
        books = repository.getAllBooks()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    TextButton(onClick = {
                        scope.launch {
                            isLoading = true
                            books = repository.getAllBooks()
                            isLoading = false
                        }
                    }) {
                        Text("Kütüphanem")
                    }
                },
                actions = {
                    // KİRALAMALARIM BUTONU
                    IconButton(onClick = onNavigateToMyBorrows) {
                        Icon(Icons.Default.Add, contentDescription = "Kiralamalarım")
                    }
                    IconButton(onClick = {
                        scope.launch {
                            books = repository.searchBooks(searchQuery)
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Ara")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Çıkış")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Ekle")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Arama Kutusu
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Kitap veya Yazar Ara...") },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        scope.launch { books = repository.searchBooks(searchQuery) }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                }
            )

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
                                    books = repository.getAllBooks()
                                }
                            },
                            onUpdateClick = {
                                showEditDialog = book
                            },
                            onBorrowClick = {
                                scope.launch {
                                    repository.borrowBook(book)
                                    books = repository.getAllBooks() // Stok güncellendiği için listeyi tazele
                                }
                            }
                        )
                    }
                }
            }
        }

        // KİTAP EKLEME DİYALOĞU
        if (showAddDialog) {
            BookDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { newBook ->
                    scope.launch {
                        repository.addBook(newBook)
                        books = repository.getAllBooks()
                        showAddDialog = false
                    }
                }
            )
        }

        // KİTAP DÜZENLEME DİYALOĞU
        if (showEditDialog != null) {
            BookDialog(
                initialBook = showEditDialog,
                onDismiss = { showEditDialog = null },
                onConfirm = { updatedBook ->
                    scope.launch {
                        repository.updateBook(updatedBook)
                        books = repository.getAllBooks()
                        showEditDialog = null
                    }
                }
            )
        }
    }
}

@Composable
fun BookDialog(
    initialBook: Book? = null,
    onDismiss: () -> Unit,
    onConfirm: (Book) -> Unit
) {
    var title by remember { mutableStateOf(initialBook?.title ?: "") }
    var author by remember { mutableStateOf(initialBook?.author ?: "") }
    var category by remember { mutableStateOf(initialBook?.category ?: "") }
    var pageCount by remember { mutableStateOf(initialBook?.page_count?.toString() ?: "0") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialBook == null) "Yeni Kitap Ekle" else "Kitabı Düzenle") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Kitap Adı") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = author, onValueChange = { author = it }, label = { Text("Yazar") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Kategori") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = pageCount, onValueChange = { pageCount = it }, label = { Text("Sayfa Sayısı") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(
                    initialBook?.copy(
                        title = title,
                        author = author,
                        category = category,
                        page_count = pageCount.toIntOrNull() ?: 0
                    ) ?: Book(
                        title = title,
                        author = author,
                        category = category,
                        page_count = pageCount.toIntOrNull() ?: 0
                    )
                )
            }) {
                Text("Kaydet")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("İptal") }
        }
    )
}
