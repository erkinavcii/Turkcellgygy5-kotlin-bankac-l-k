package com.example.ticketapp.ui.admin

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

// --- Admin Renk Paleti ---
private val AdminAccent = Color(0xFFFF6B35)       // Ateş Turuncusu
private val AdminSecondary = Color(0xFFFFD700)    // Altın Sarısı
private val AdminBg = Color(0xFF0A0E1A)           // Derin Lacivert Arka Plan
private val AdminCard = Color(0xFF141928)         // Kart Arka Planı
private val AdminBorder = Color(0xFFFF6B35).copy(alpha = 0.3f)

@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel = koinViewModel(),
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val eventsState = viewModel.eventsState
    val operationState = viewModel.operationState

    // Operation state feedback
    LaunchedEffect(operationState) {
        when (operationState) {
            is AdminOperationState.Success -> {
                Toast.makeText(context, operationState.message, Toast.LENGTH_SHORT).show()
                viewModel.resetOperationState()
            }
            is AdminOperationState.Error -> {
                Toast.makeText(context, "Hata: ${operationState.message}", Toast.LENGTH_LONG).show()
                viewModel.resetOperationState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AdminBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // --- Admin Header ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(AdminAccent.copy(alpha = 0.15f), Color.Transparent)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Admin badge
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(AdminAccent.copy(alpha = 0.2f), CircleShape)
                            .border(1.5.dp, AdminAccent.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = AdminAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Yönetici Paneli",
                            color = AdminAccent,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = viewModel.adminEmail,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }

                // Logout button
                IconButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color(0xFFFF2A6D).copy(alpha = 0.15f), CircleShape)
                        .border(1.dp, Color(0xFFFF2A6D).copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Çıkış Yap",
                        tint = Color(0xFFFF2A6D)
                    )
                }
            }

            // --- Stats Row ---
            val totalEvents = (eventsState as? AdminEventsState.Success)?.events?.size ?: 0
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AdminStatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.DateRange,
                    label = "Toplam Etkinlik",
                    value = "$totalEvents",
                    color = AdminAccent
                )
                AdminStatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Build,
                    label = "Panel Rolu",
                    value = viewModel.adminRole,
                    color = AdminSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Etkinlik Listesi Başlığı + Ekle Butonu ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Etkinlik Yönetimi",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = { viewModel.showCreateDialog() },
                    colors = ButtonDefaults.buttonColors(containerColor = AdminAccent),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Yeni Etkinlik", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            // --- Event List ---
            when (eventsState) {
                is AdminEventsState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AdminAccent)
                    }
                }
                is AdminEventsState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(eventsState.message, color = Color.Red, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { viewModel.loadEvents() },
                                colors = ButtonDefaults.buttonColors(containerColor = AdminAccent)
                            ) { Text("Tekrar Dene") }
                        }
                    }
                }
                is AdminEventsState.Success -> {
                    if (eventsState.events.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Henüz etkinlik yok.", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(eventsState.events) { event ->
                                AdminEventCard(
                                    event = event,
                                    onEdit = { viewModel.showEditDialog(event) },
                                    onDelete = { viewModel.deleteEvent(event.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Loading overlay for operations
        if (operationState is AdminOperationState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = AdminAccent)
            }
        }
    }

    // --- Create/Edit Dialog ---
    if (viewModel.showDialog) {
        AdminEventDialog(
            isEdit = viewModel.editingEvent != null,
            initialName = viewModel.editingEvent?.name ?: "",
            initialDescription = viewModel.editingEvent?.description ?: "",
            initialPlace = viewModel.editingEvent?.venue ?: "",
            initialStartsAt = viewModel.editingEvent?.startsAt ?: "",
            initialEndsAt = viewModel.editingEvent?.endsAt ?: "",
            onDismiss = { viewModel.hideDialog() },
            onConfirm = { name, description, place, startsAt, endsAt ->
                if (viewModel.editingEvent != null) {
                    viewModel.updateEvent(viewModel.editingEvent!!.id, name, description, place, startsAt, endsAt)
                } else {
                    viewModel.createEvent(name, description, place, startsAt, endsAt)
                }
            }
        )
    }
}

@Composable
private fun AdminStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AdminCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, color.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(label, color = Color.Gray, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun AdminEventCard(
    event: com.example.ticketapp.domain.model.Event,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, AdminBorder, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AdminCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Place,
                            contentDescription = null,
                            tint = AdminAccent,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(event.venue, color = Color.LightGray, fontSize = 12.sp)
                    }
                }

                // Action buttons
                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(36.dp)
                            .background(AdminSecondary.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Düzenle",
                            tint = AdminSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.Red.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Sil",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Delete confirmation
            AnimatedVisibility(
                visible = showDeleteConfirm,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(Color.Red.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Bu etkinliği silmek istiyor musun?",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Row {
                        TextButton(onClick = { showDeleteConfirm = false }) {
                            Text("İptal", color = Color.Gray, fontSize = 12.sp)
                        }
                        Button(
                            onClick = {
                                showDeleteConfirm = false
                                onDelete()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Sil", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminEventDialog(
    isEdit: Boolean,
    initialName: String,
    initialDescription: String,
    initialPlace: String,
    initialStartsAt: String,
    initialEndsAt: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var description by remember { mutableStateOf(initialDescription) }
    var place by remember { mutableStateOf(initialPlace) }
    var startsAt by remember { mutableStateOf(initialStartsAt) }
    var endsAt by remember { mutableStateOf(initialEndsAt) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AdminCard,
        title = {
            Text(
                text = if (isEdit) "✏️ Etkinliği Düzenle" else "➕ Yeni Etkinlik",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AdminTextField("Etkinlik Adı *", name) { name = it }
                AdminTextField("Açıklama", description) { description = it }
                AdminTextField("Mekan *", place) { place = it }
                AdminTextField("Başlangıç (ör: 2026-12-31T20:00:00Z) *", startsAt) { startsAt = it }
                AdminTextField("Bitiş (ör: 2026-12-31T23:00:00Z) *", endsAt) { endsAt = it }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && place.isNotBlank() && startsAt.isNotBlank() && endsAt.isNotBlank()) {
                        onConfirm(name, description, place, startsAt, endsAt)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AdminAccent)
            ) {
                Text(if (isEdit) "Güncelle" else "Oluştur")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal", color = Color.Gray)
            }
        }
    )
}

@Composable
private fun AdminTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 11.sp) },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AdminAccent,
            unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f),
            focusedLabelColor = AdminAccent,
            unfocusedLabelColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = AdminAccent
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = label.contains("Adı") || label.contains("Mekan") || label.contains("Başlangıç") || label.contains("Bitiş")
    )
}
