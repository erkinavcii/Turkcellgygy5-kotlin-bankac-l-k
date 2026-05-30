package com.example.ticketapp.ui.dashboard

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ticketapp.domain.model.Event
import com.example.ticketapp.domain.model.Ticket
import com.example.ticketapp.domain.model.TicketType
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

// Custom ticket stub shape with concave notches
class TicketShape(private val notchRadius: Dp, private val notchYPercentage: Float = 0.68f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val notchRadiusPx = with(density) { notchRadius.toPx() }
            val notchY = size.height * notchYPercentage
            
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, notchY - notchRadiusPx)
            // Right notch (concave cut-in)
            arcTo(
                rect = Rect(
                    left = size.width - notchRadiusPx,
                    top = notchY - notchRadiusPx,
                    right = size.width + notchRadiusPx,
                    bottom = notchY + notchRadiusPx
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            lineTo(0f, notchY + notchRadiusPx)
            // Left notch (concave cut-in)
            arcTo(
                rect = Rect(
                    left = -notchRadiusPx,
                    top = notchY - notchRadiusPx,
                    right = notchRadiusPx,
                    bottom = notchY + notchRadiusPx
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = -180f,
                forceMoveTo = false
            )
            close()
        }
        return Outline.Generic(path)
    }
}

// Beautiful dotted divider
@Composable
fun DottedLine(modifier: Modifier = Modifier, color: Color = Color.Gray, strokeWidth: Float = 3f) {
    Canvas(modifier = modifier) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = strokeWidth,
            pathEffect = pathEffect
        )
    }
}

// Deterministic geometric mock QR code
@Composable
fun MockQRCode(
    modifier: Modifier = Modifier,
    contentString: String
) {
    Canvas(modifier = modifier.shadow(2.dp, RoundedCornerShape(4.dp))) {
        val sizePx = size.width
        val cells = 12
        val cellSize = sizePx / cells
        val hash = contentString.hashCode()
        
        // Background
        drawRect(Color.White)
        
        // Finder patterns in three corners
        // Top-Left
        drawRect(
            color = Color.Black,
            topLeft = Offset(0f, 0f),
            size = Size(cellSize * 3, cellSize * 3)
        )
        drawRect(
            color = Color.White,
            topLeft = Offset(cellSize, cellSize),
            size = Size(cellSize, cellSize)
        )
        
        // Top-Right
        drawRect(
            color = Color.Black,
            topLeft = Offset(sizePx - cellSize * 3, 0f),
            size = Size(cellSize * 3, cellSize * 3)
        )
        drawRect(
            color = Color.White,
            topLeft = Offset(sizePx - cellSize * 2, cellSize),
            size = Size(cellSize, cellSize)
        )
        
        // Bottom-Left
        drawRect(
            color = Color.Black,
            topLeft = Offset(0f, sizePx - cellSize * 3),
            size = Size(cellSize * 3, cellSize * 3)
        )
        drawRect(
            color = Color.White,
            topLeft = Offset(cellSize, sizePx - cellSize * 2),
            size = Size(cellSize, cellSize)
        )
        
        // Fill data based on hash
        for (r in 0 until cells) {
            for (c in 0 until cells) {
                val isTopLeft = r < 3 && c < 3
                val isTopRight = r < 3 && c >= cells - 3
                val isBottomLeft = r >= cells - 3 && c < 3
                
                if (!isTopLeft && !isTopRight && !isBottomLeft) {
                    val bitIndex = r * cells + c
                    val isBlack = ((hash ushr (bitIndex % 32)) and 1) != 0
                    if (isBlack) {
                        drawRect(
                            color = Color.Black,
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                    }
                }
            }
        }
    }
}

// Format ISO date
fun formatIsoDate(isoString: String): String {
    return try {
        // e.g. 2026-05-20T21:00:00Z
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val date = parser.parse(isoString) ?: return isoString
        val formatter = SimpleDateFormat("dd MMMM yyyy, EEEE HH:mm", Locale("tr"))
        formatter.format(date)
    } catch (e: Exception) {
        isoString
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val eventsState = viewModel.eventsState
    val ticketsState = viewModel.ticketsState
    val checkoutState = viewModel.checkoutState
    
    val userName = viewModel.sessionManager.session?.user?.firstName ?: "Misafir"
    
    // Purchase modal state
    var selectedEventForPurchase by remember { mutableStateOf<Event?>(null) }
    var selectedTicketTypeForPurchase by remember { mutableStateOf<TicketType?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF070B19)) // Premium Obsidian background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Sleek Top Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Merhaba, $userName 👋",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Bugün hangi etkinliğe gidiyoruz?",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                
                // Glowing Logout Button
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

            // Custom Capsule Tab Controller
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .background(Color(0xFF13182C), shape = RoundedCornerShape(24.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Etkinlikler", "Biletlerim").forEachIndexed { index, title ->
                    val selected = viewModel.selectedTab == index
                    val activeBgColor by animateColorAsState(
                        targetValue = if (selected) Color(0xFF8A2BE2) else Color.Transparent,
                        animationSpec = tween(300), label = "tabBg"
                    )
                    val activeTextColor by animateColorAsState(
                        targetValue = if (selected) Color.White else Color.Gray,
                        animationSpec = tween(300), label = "tabText"
                    )
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(activeBgColor, shape = RoundedCornerShape(20.dp))
                            .clickable { viewModel.selectedTab = index }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = if (index == 0) Icons.Default.DateRange else Icons.Default.Star,
                                contentDescription = null,
                                tint = activeTextColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = title,
                                color = activeTextColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = viewModel.selectedTab,
                    transitionSpec = {
                        if (targetState > initialState) {
                            slideInHorizontally { width -> width } + fadeIn() with
                                    slideOutHorizontally { width -> -width } + fadeOut()
                        } else {
                            slideInHorizontally { width -> -width } + fadeIn() with
                                    slideOutHorizontally { width -> width } + fadeOut()
                        }.using(SizeTransform(clip = false))
                    }, label = "tabTransition"
                ) { targetTab ->
                    when (targetTab) {
                        0 -> EventsTabContent(
                            eventsState = eventsState,
                            onPurchaseClick = { event, type ->
                                selectedEventForPurchase = event
                                selectedTicketTypeForPurchase = type
                            },
                            onRefresh = { viewModel.loadDashboardData() }
                        )
                        1 -> TicketsTabContent(
                            ticketsState = ticketsState,
                            eventsState = eventsState,
                            onRefresh = { viewModel.loadDashboardData() }
                        )
                    }
                }
            }
        }

        // Checkout Dialog/Modal
        if (selectedEventForPurchase != null && selectedTicketTypeForPurchase != null) {
            CheckoutDialog(
                event = selectedEventForPurchase!!,
                ticketType = selectedTicketTypeForPurchase!!,
                checkoutState = checkoutState,
                onDismiss = {
                    viewModel.resetCheckoutState()
                    selectedEventForPurchase = null
                    selectedTicketTypeForPurchase = null
                },
                onConfirmPurchase = { qty ->
                    viewModel.purchaseTicket(selectedTicketTypeForPurchase!!.id, qty)
                },
                onGoToTickets = {
                    viewModel.resetCheckoutState()
                    selectedEventForPurchase = null
                    selectedTicketTypeForPurchase = null
                    viewModel.selectedTab = 1 // Switch to My Tickets
                }
            )
        }
    }
}

@Composable
fun EventsTabContent(
    eventsState: EventsState,
    onPurchaseClick: (Event, TicketType) -> Unit,
    onRefresh: () -> Unit
) {
    when (eventsState) {
        is EventsState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF00F5FF))
            }
        }
        is EventsState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp), 
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = eventsState.message, color = Color.Red, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRefresh, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))) {
                        Text("Tekrar Dene")
                    }
                }
            }
        }
        is EventsState.Success -> {
            val events = eventsState.events
            if (events.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Yakın zamanda planlanmış etkinlik bulunmuyor.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(events) { event ->
                        EventCard(event = event, onPurchaseClick = onPurchaseClick)
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    onPurchaseClick: (Event, TicketType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(Color(0xFF8A2BE2).copy(0.4f), Color(0xFF00F5FF).copy(0.1f))),
                RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF13182C))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title & Expand Arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Detay",
                        tint = Color(0xFF00F5FF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Venue Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color(0xFF00F5FF),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = event.venue,
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF8A2BE2),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = formatIsoDate(event.startsAt),
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }

            // Expanded description
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = event.description ?: "Bu etkinlik için açıklama bulunmuyor.",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Divider(color = Color.White.copy(0.08f), modifier = Modifier.padding(vertical = 12.dp))

            // Ticket Options Title
            Text(
                text = "Bilet Seçenekleri",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Ticket Types List
            if (event.ticketTypes.isEmpty()) {
                Text(
                    text = "Satışta bilet bulunmamaktadır.",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    event.ticketTypes.forEach { type ->
                        val isSoldOut = type.remaining <= 0
                        val isLowRemaining = type.remaining in 1..15
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF070B19).copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .border(0.5.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = type.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${type.priceCents / 100} TL",
                                    color = Color(0xFF00F5FF),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Remaining Badge
                                when {
                                    isSoldOut -> {
                                        Text(
                                            text = "Tükendi",
                                            color = Color.Red,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    isLowRemaining -> {
                                        Text(
                                            text = "Son ${type.remaining}!",
                                            color = Color(0xFFFF9800),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                    else -> {
                                        Text(
                                            text = "${type.remaining} Kalan",
                                            color = Color.Green,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                    }
                                }

                                Button(
                                    onClick = { onPurchaseClick(event, type) },
                                    enabled = !isSoldOut,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF8A2BE2),
                                        disabledContainerColor = Color.Gray.copy(0.2f)
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier
                                        .height(32.dp)
                                        .shadow(if (!isSoldOut) 4.dp else 0.dp, RoundedCornerShape(6.dp))
                                ) {
                                    Text(
                                        text = "Bilet Al",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSoldOut) Color.DarkGray else Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketsTabContent(
    ticketsState: TicketsState,
    eventsState: EventsState,
    onRefresh: () -> Unit
) {
    when (ticketsState) {
        is TicketsState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF00F5FF))
            }
        }
        is TicketsState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp), 
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = ticketsState.message, color = Color.Red, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onRefresh, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))) {
                        Text("Tekrar Dene")
                    }
                }
            }
        }
        is TicketsState.Success -> {
            val tickets = ticketsState.tickets
            val events = (eventsState as? EventsState.Success)?.events ?: emptyList()
            
            if (tickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Gray.copy(0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Henüz satın aldığın bir bilet bulunmuyor.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(tickets) { ticket ->
                        // Map ticketTypeId to actual Event and TicketType
                        var matchingEvent: Event? = null
                        var matchingType: TicketType? = null
                        
                        for (e in events) {
                            val type = e.ticketTypes.find { it.id == ticket.ticketTypeId }
                            if (type != null) {
                                matchingEvent = e
                                matchingType = type
                                break
                            }
                        }
                        
                        TicketCard(
                            ticket = ticket,
                            event = matchingEvent,
                            ticketType = matchingType
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TicketCard(
    ticket: Ticket,
    event: Event?,
    ticketType: TicketType?
) {
    val isUsed = ticket.status.uppercase() == "USED"
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .shadow(6.dp, TicketShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF13182C),
                        Color(0xFF0F1222)
                    )
                ),
                shape = TicketShape(12.dp)
            )
            .border(
                0.5.dp, 
                Color.White.copy(0.08f), 
                TicketShape(12.dp)
            )
    ) {
        // Main Body (Top Part - 68%)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.68f)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Event details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event?.name ?: "Bilinmeyen Etkinlik",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = ticketType?.name ?: "Bilet Tipi",
                        color = Color(0xFF00F5FF),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Active / Used Badge
                Box(
                    modifier = Modifier
                        .background(
                            if (isUsed) Color.Gray.copy(alpha = 0.2f) else Color(0xFF05FF80).copy(alpha = 0.15f),
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (isUsed) Color.Gray.copy(alpha = 0.4f) else Color(0xFF05FF80).copy(alpha = 0.5f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isUsed) "KULLANILDI" else "AKTİF",
                        color = if (isUsed) Color.Gray else Color(0xFF05FF80),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Venue Location info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = event?.venue ?: "Bilinmeyen Mekan",
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Start Date info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = event?.startsAt?.let { formatIsoDate(it) } ?: "Bilinmeyen Tarih",
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            
            // Ticket ID Serial
            Text(
                text = "Bilet ID: #${ticket.id.take(8).uppercase()}",
                color = Color.Gray,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Perforation separator line (at 68% height)
        DottedLine(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.68f)
                .align(Alignment.TopCenter)
                .offset(y = 12.dp),
            color = Color.White.copy(0.12f),
            strokeWidth = 4f
        )

        // Stub (Bottom Part - 32%)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.32f)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "KAPI GİRİŞİ",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "QR KODU OKUTUN",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Kişiye Özel Bilet",
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }

            // Beautiful Geometric Mock QR Code
            MockQRCode(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentString = ticket.qrCode
            )
        }
    }
}

// Interactive Checkout Modal
@Composable
fun CheckoutDialog(
    event: Event,
    ticketType: TicketType,
    checkoutState: CheckoutState,
    onDismiss: () -> Unit,
    onConfirmPurchase: (Int) -> Unit,
    onGoToTickets: () -> Unit
) {
    var quantity by remember { mutableStateOf(1) }
    val totalPriceCents = ticketType.priceCents * quantity
    
    Dialog(
        onDismissRequest = { if (checkoutState !is CheckoutState.Processing) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .border(1.dp, Color(0xFF00F5FF).copy(0.2f), RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF13182C))
        ) {
            Box(modifier = Modifier.padding(24.dp)) {
                when (checkoutState) {
                    is CheckoutState.Idle -> {
                        Column {
                            // Dialog Title
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Bilet Satın Al",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = onDismiss) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Kapat", tint = Color.Gray)
                                }
                            }
                            
                            Divider(color = Color.White.copy(0.08f), modifier = Modifier.padding(vertical = 12.dp))
                            
                            // Event Details
                            Text(text = event.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = ticketType.name, color = Color(0xFF00F5FF), fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Birim Fiyat: ${ticketType.priceCents / 100} TL",
                                color = Color.LightGray,
                                fontSize = 13.sp
                            )
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            // Quantity Picker
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF070B19), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Bilet Adeti", color = Color.White, fontWeight = FontWeight.SemiBold)
                                
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { if (quantity > 1) quantity-- },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Color.White.copy(0.05f), CircleShape)
                                    ) {
                                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Azalt", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                    
                                    Text(
                                        text = quantity.toString(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    
                                    IconButton(
                                        onClick = { if (quantity < ticketType.remaining) quantity++ },
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Color.White.copy(0.05f), CircleShape)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Arttır", tint = Color.White, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Summary
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Toplam Tutar", color = Color.Gray, fontSize = 14.sp)
                                Text(
                                    text = "${totalPriceCents / 100} TL",
                                    color = Color(0xFF05FF80),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            // Action Button
                            Button(
                                onClick = { onConfirmPurchase(quantity) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .shadow(6.dp, RoundedCornerShape(12.dp)),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Ödemeyi Onayla", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                    
                    is CheckoutState.Processing -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF00F5FF), modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(text = "İşleminiz güvenli şekilde yapılıyor...", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Lütfen pencereyi kapatmayın", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                    
                    is CheckoutState.Success -> {
                        val scale = remember { Animatable(0f) }
                        LaunchedEffect(Unit) {
                            scale.animateTo(
                                targetValue = 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                        
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Glowing Checkmark
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(Color(0xFF05FF80).copy(alpha = 0.15f), CircleShape)
                                    .border(2.dp, Color(0xFF05FF80), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Başarılı",
                                    tint = Color(0xFF05FF80),
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "Biletiniz Hazır! 🎉",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "Ödeme işlemi başarıyla tamamlandı. Biletinizi 'Biletlerim' sekmesinden görüntüleyebilirsiniz.",
                                color = Color.Gray,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Button(
                                onClick = onGoToTickets,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF05FF80)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Biletlerime Git", color = Color(0xFF070B19), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                    
                    is CheckoutState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(Color.Red.copy(alpha = 0.15f), CircleShape)
                                    .border(2.dp, Color.Red, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Hata",
                                    tint = Color.Red,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "İşlem Başarısız",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = checkoutState.message,
                                color = Color.Gray,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    border = BorderStroke(1.dp, Color.Gray),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Kapat", color = Color.White)
                                }
                                
                                Button(
                                    onClick = { onConfirmPurchase(quantity) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Tekrar Dene", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
