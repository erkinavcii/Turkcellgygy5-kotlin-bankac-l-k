package com.example.turkcellintro.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.turkcellintro.data.model.BorrowRecord
import com.example.turkcellintro.data.repository.BookRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBorrowsScreen(
    repository: BookRepository,
    onBack: () -> Unit
) {
    var borrows by remember { mutableStateOf<List<BorrowRecord>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        borrows = repository.getMyBorrows()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kiralamalarım") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (borrows.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Henüz ödünç aldığınız bir kitap yok.", fontSize = 16.sp)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                items(borrows) { record ->
                    BorrowRecordCard(record)
                }
            }
        }
    }
}

@Composable
fun BorrowRecordCard(record: BorrowRecord) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Kitap ID: ${record.book_id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Ödünç Alma: ${record.borrow_date ?: "N/A"}", fontSize = 14.sp)
            Text(text = "Son İade: ${record.due_date}", fontSize = 14.sp, color = Color.Red)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Surface(
                color = if (record.status == "active") Color(0xFF4CAF50) else Color.Gray,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = if (record.status == "active") "AKTİF" else "İADE EDİLDİ",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
