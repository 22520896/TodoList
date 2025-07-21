package com.example.todolist.ui.event

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist.data.entity.Event
import com.example.todolist.ui.todo.TodoModalViewModel
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.EventModalViewModel
import com.example.todolist.viewmodel.EventViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    eventViewModel: EventViewModel = hiltViewModel(),
    commonViewModel: CommonViewModel,
    modifier: Modifier
) {
    val currentMonth: YearMonth = YearMonth.now()
    val events by eventViewModel.allEvents.collectAsState(initial = emptyList())
    val dateFormat by commonViewModel.dateFormat.collectAsStateWithLifecycle()
    val timeFormat by commonViewModel.timeFormat.collectAsStateWithLifecycle()
    val color by commonViewModel.color.collectAsStateWithLifecycle()
    val eventModalViewModel: EventModalViewModel = hiltViewModel()
    var month by remember { mutableStateOf(currentMonth) }
    val today = LocalDate.now()
    var showModalBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val firstDayOfMonth = month.atDay(1)
    val daysInMonth = month.lengthOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val totalCells = ((firstDayOfWeek + daysInMonth + 6) / 7) * 7
    val scope = rememberCoroutineScope()
    val eventsInCurrentMonth = events.filter {
        it.startDate.year == month.year && it.startDate.month == month.month
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White).then(modifier)// Đặt nền trắng cho toàn bộ màn hình
    ) {
        // Phần tiêu đề tháng/năm (cố định)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(android.graphics.Color.parseColor(color)))
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { month = month.minusMonths(1) }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Previous",
                    tint = Color.White
                )
            }

            Text(
                text = month.format(
                    DateTimeFormatter.ofPattern(
                        "MMMM yyyy",
                        Locale.getDefault()
                    )
                ),
                color = Color.White, // Sử dụng màu từ commonViewModel
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            IconButton(onClick = { month = month.plusMonths(1) }) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    tint = Color.White
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        // Phần ngày trong tuần (cố định)
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach {
                Text(
                    text = it,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(Modifier.height(3.dp))

        // Lưới ngày (cố định)
        Column {
            for (week in 0 until totalCells / 7) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (day in 0..6) {
                        val cellIndex = week * 7 + day
                        val date =
                            if (cellIndex >= firstDayOfWeek && cellIndex < firstDayOfWeek + daysInMonth) {
                                month.atDay(cellIndex - firstDayOfWeek + 1)
                            } else null

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CalendarDayCellModern(
                                date = date,
                                today = today,
                                color = color,
                                hasEvent = date != null && events.any {
                                    it.startDate <= date && it.endDate >= date
                                }
                            )
                        }
                    }
                }
            }
        }

        // Tiêu đề sự kiện (cố định)
        Spacer(modifier = Modifier.height(13.dp))
        Text(
            text = "Sự kiện trong tháng",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(5.dp))

        // Danh sách sự kiện (cuộn được)
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(eventsInCurrentMonth) { event ->
                EventItemWithSwipe(
                    dateFormat = dateFormat,
                    timeFormat = timeFormat,
                    event = event,
                    onClick = {
                        eventModalViewModel.startEditEvent(event)
                        showModalBottomSheet = true
                        scope.launch { sheetState.show() }
                    },
                    onDeleteConfirmed = {
                        eventViewModel.deleteEvent(event)
                    }
                )
            }
        }
    }


    if (showModalBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showModalBottomSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            scrimColor = Color.Black.copy(alpha = 0.3f),
            dragHandle = {}
        ) {
            EventModal(
                viewModel = eventModalViewModel,
                commonViewModel = commonViewModel,
                onDismiss = { showModalBottomSheet = false },
            )
        }
    }
}

@Composable
fun CalendarDayCellModern(
    date: LocalDate?,
    today: LocalDate = LocalDate.now(),
    color: String,
    hasEvent: Boolean
) {
    if (date != null) {
        val isToday = date == today

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (isToday) Color(android.graphics.Color.parseColor(color)) else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    color = if (isToday) Color.White else Color.Black,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                )
            }

            if (hasEvent) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = event.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Từ ${event.startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} " +
                        "đến ${event.endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventItemWithSwipe(
    event: Event,
    dateFormat: String,
    timeFormat: String,
    onClick: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatterDate = DateTimeFormatter.ofPattern(dateFormat)
    val formatterTime = DateTimeFormatter.ofPattern(timeFormat)

    var showConfirmDialog by remember { mutableStateOf(false) }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                showConfirmDialog = true
                false
            } else true
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
            }
        },
        dismissContent = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .shadow(3.dp, RoundedCornerShape(18.dp))
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .clickable { onClick() }
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                        ) {
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = event.detail,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f) // Đảm bảo đủ không gian cho ngày tháng
                                .padding(start = 8.dp),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${event.startTime.format(formatterTime)} - ${event.startDate.format(formatterDate)}",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.End
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Box(
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(end = 43.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = Color.Gray
                                    )
                                }
                            }

                            Text(
                                text = "${event.endTime.format(formatterTime)} - ${event.endDate.format(formatterDate)}",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    )

    // Dialog xác nhận xóa
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    onDeleteConfirmed()
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                }) { Text("Hủy") }
            },
            title = {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "Xác nhận xóa",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            },
            text = { Text("Bạn có chắc chắn muốn xóa sự kiện này không?") }
        )
    }
}