package com.example.todolist.ui.event//package com.example.todolist.ui.event
//
//import androidx.compose.animation.core.Animatable
//import androidx.compose.animation.core.VectorConverter
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.detectHorizontalDragGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.ArrowForward
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import java.time.LocalDate
//import java.time.YearMonth
//import java.time.format.DateTimeFormatter
//import java.util.Locale
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.unit.IntOffset
//import androidx.navigation.NavController
//import com.example.todolist.data.entity.Event
//import com.example.todolist.viewmodel.EventViewModel
//
//import kotlinx.coroutines.launch
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CalendarScreen(
//    navController: NavController,
//    eventViewModel: EventViewModel,
//    currentMonth: YearMonth = YearMonth.now(),
//    events: List<Event>
//) {
//    var month by remember { mutableStateOf(currentMonth) }
//    val today = LocalDate.now()
//    var showModalBottomSheet by remember { mutableStateOf(false) }
//
//    val firstDayOfMonth = month.atDay(1)
//    val daysInMonth = month.lengthOfMonth()
//    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
//    val totalCells = ((firstDayOfWeek + daysInMonth + 6) / 7) * 7
//
//    val eventsInCurrentMonth = events.filter {
//        it.startDate.year == month.year && it.startDate.month == month.month
//    }
//
//    Scaffold(
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {
//                    eventViewModel.clearInputs()
//                    showModalBottomSheet = true
//                },
//                containerColor = Color(0xFF388E3C)
//            ) {
//                Text("+", color = Color.White, fontSize = 24.sp)
//            }
//        }
//    ) { paddingValues ->
//
////        if (!showModalBottomSheet) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            item {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color(0xFF388E3C))
//                        .padding(8.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    IconButton(onClick = { month = month.minusMonths(1) }) {
//                        Icon(
//                            Icons.Default.ArrowBack,
//                            contentDescription = "Previous",
//                            tint = Color.White
//                        )
//                    }
//
//                    Text(
//                        text = month.format(
//                            DateTimeFormatter.ofPattern(
//                                "MMMM yyyy",
//                                Locale.getDefault()
//                            )
//                        ),
//                        color = Color.White,
//                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
//                    )
//
//                    IconButton(onClick = { month = month.plusMonths(1) }) {
//                        Icon(
//                            Icons.Default.ArrowForward,
//                            contentDescription = "Next",
//                            tint = Color.White
//                        )
//                    }
//                }
//                Spacer(Modifier.height(8.dp))
//            }
//
//            item {
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    listOf("S", "M", "T", "W", "T", "F", "S").forEach {
//                        Text(
//                            text = it,
//                            modifier = Modifier.weight(1f),
//                            textAlign = TextAlign.Center,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    }
//                }
//                Spacer(Modifier.height(4.dp))
//            }
//
//            items(totalCells / 7) { week ->
//                Row(modifier = Modifier.fillMaxWidth()) {
//                    for (day in 0..6) {
//                        val cellIndex = week * 7 + day
//                        val date =
//                            if (cellIndex >= firstDayOfWeek && cellIndex < firstDayOfWeek + daysInMonth) {
//                                month.atDay(cellIndex - firstDayOfWeek + 1)
//                            } else null
//
//                        Box(
//                            modifier = Modifier
//                                .weight(1f)
//                                .aspectRatio(1f),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            CalendarDayCellModern(
//                                date = date,
//                                today = today,
//                                hasEvent = date != null && events.any {
//                                    it.startDate <= date && it.endDate >= date
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//
//            item {
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = "Sự kiện trong tháng",
//                    style = MaterialTheme.typography.titleMedium,
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                )
//            }
//
//            items(eventsInCurrentMonth) { event ->
//                EventItemWithSwipe(
//                    event = event,
//                    onDelete = { eventViewModel.deleteEvent(event) },
//                    onClick = {
//                        eventViewModel.loadEvent(event)
//                        showModalBottomSheet = true
//                    }
//                )
//            }
////            }
//        }
//    }
//
//    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true
//    )
//
//    if (showModalBottomSheet) {
//        ModalBottomSheet(
//            onDismissRequest = { showModalBottomSheet = false },
//            sheetState = sheetState,
//            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
//            scrimColor = Color.Black.copy(alpha = 0.3f)
//        ) {
//            AddEditEventScreen(
//                eventViewModel = eventViewModel,
//                onCancel = { showModalBottomSheet = false },
//                onSave = { showModalBottomSheet = false }
//            )
//        }
//    }
//}
//
//
//@Composable
//fun CalendarDayCellModern(
//    date: LocalDate?,
//    today: LocalDate = LocalDate.now(),
//    hasEvent: Boolean
//) {
//    if (date != null) {
//        val isToday = date == today
//
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(36.dp)
//                    .clip(CircleShape)
//                    .background(
//                        color = if (isToday) Color(0xFF388E3C) else Color.Transparent,
//                        shape = CircleShape
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = date.dayOfMonth.toString(),
//                    color = if (isToday) Color.White else Color.Black,
//                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
//                )
//            }
//
//            if (hasEvent) {
//                Box(
//                    modifier = Modifier
//                        .size(5.dp)
//                        .clip(CircleShape)
//                        .background(Color.Gray)
//                        .padding(top = 2.dp)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun EventCard(event: Event, onClick: () -> Unit = {}) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onClick() },
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(4.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
//    ) {
//        Column(modifier = Modifier.padding(12.dp)) {
//            Text(text = event.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = "Từ ${event.startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} " +
//                        "đến ${event.endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
//                style = MaterialTheme.typography.bodySmall
//            )
//        }
//    }
//}
//
//
//@Composable
//fun EventItemWithSwipe(
//    event: Event,
//    onDelete: () -> Unit,
//    onClick: () -> Unit
//) {
//    val offsetX = remember { Animatable(0f, Float.VectorConverter) }
//    val scope = rememberCoroutineScope()
//    var showDelete by remember { mutableStateOf(false) }
//
//    val maxSwipe = with(LocalDensity.current) { 80.dp.toPx() }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 4.dp) // SAME padding with EventCard
//            .background(Color.Transparent)
//            .clip(RoundedCornerShape(12.dp)) // SAME corner radius
//            .pointerInput(Unit) {
//                detectHorizontalDragGestures(
//                    onDragEnd = {
//                        scope.launch {
//                            if (offsetX.value < -maxSwipe / 2) {
//                                offsetX.animateTo(-maxSwipe)
//                                showDelete = true
//                            } else {
//                                offsetX.animateTo(0f)
//                                showDelete = false
//                            }
//                        }
//                    },
//                    onHorizontalDrag = { _, dragAmount ->
//                        val newOffset = offsetX.value + dragAmount
//                        if (newOffset <= 0f) {
//                            scope.launch {
//                                offsetX.snapTo(newOffset.coerceAtLeast(-maxSwipe))
//                            }
//                        }
//                    }
//                )
//            }
//    ) {
//        // Nền đỏ và icon thùng rác
//        Box(
//            modifier = Modifier
//                .matchParentSize()
//                .clip(RoundedCornerShape(12.dp))
//                .background(Color.Red),
//            contentAlignment = Alignment.CenterEnd
//        ) {
//            if (showDelete) {
//                IconButton(onClick = {
//                    onDelete()
//                    scope.launch {
//                        offsetX.snapTo(0f)
//                        showDelete = false
//                    }
//                }) {
//                    Icon(Icons.Default.Delete, contentDescription = "Xoá", tint = Color.White)
//                }
//            }
//        }
//
//        // Thẻ sự kiện
//        Box(
//            modifier = Modifier.offset { IntOffset(offsetX.value.toInt(), 0) }
//        ) {
//            EventCard(event = event, onClick = onClick)
//        }
//    }
//}