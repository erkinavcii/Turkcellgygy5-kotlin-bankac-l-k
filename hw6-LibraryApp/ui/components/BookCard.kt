package com.example.turkcellintro.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.turkcellintro.data.model.Book

/**
 * ÖDEV 3: KİTAP KART TASARIMI (Composable)
 */
@Composable
fun BookCard(
    book: Book,
    onDeleteClick: () -> Unit = {},
    onUpdateClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = book.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Yazar: ${book.author}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Kategori: ${book.category}", fontSize = 14.sp)
                Text(text = "Sayfa: ${book.page_count}", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Stok Durumu Göstergesi
            val statusColor = if (book.available_copies > 0) Color(0xFF4CAF50) else Color.Red
            val statusText = if (book.available_copies > 0) "Mevcut (${book.available_copies})" else "Stokta Yok"
            
            Text(
                text = statusText,
                color = statusColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onUpdateClick) {
                    Text("Düzenle")
                }
                TextButton(onClick = onDeleteClick, colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) {
                    Text("Sil")
                }
            }
        }
    }
}
