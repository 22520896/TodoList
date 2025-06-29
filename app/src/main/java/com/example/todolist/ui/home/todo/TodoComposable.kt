package com.example.todolist.ui.home.todo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist.R
import com.example.todolist.data.entity.Todo
import com.example.todolist.ui.CommonTitleField
import com.example.todolist.viewmodel.DateFilter
import com.example.todolist.viewmodel.TodoViewModel
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

//@Composable
//@OptIn(ExperimentalMaterial3Api::class)
//fun TodoTopNavBar(
//    selectedFilter: DateFilter,
//    onFilterChange: (DateFilter) -> Unit
//) {
//    val items = DateFilter.values()
//
//    CenterAlignedTopAppBar(
//        modifier = Modifier.padding(top = 8.dp),
//        colors  = TopAppBarDefaults.centerAlignedTopAppBarColors(
//            containerColor = Color.Transparent
//        ),
//        title = {                    // <-- truyền Row vào title
//            SingleChoiceSegmentedButtonRow {
//                items.forEachIndexed { idx, f ->
//                    SegmentedButton(
//                        selected = f == selectedFilter,
//                        onClick  = { onFilterChange(f) },
//                        shape    = SegmentedButtonDefaults.itemShape(idx, items.size),
//                        colors   = SegmentedButtonDefaults.colors(
//                            activeContainerColor   = Color(0xFFFF9800),
//                            inactiveContainerColor = Color(0xFFF2F2F2)
//                        )
//                    ) {
//                        Text(f.label, fontSize = 13.sp)
//                    }
//                }
//            }
//        }
//    )
//}
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TodoTopNavBar(
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    val items = listOf("Ngày", "Tuần", "Tháng")

    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .width(250.dp)
                    .height(35.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFF2F2F2))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, label ->
                    val selected = selectedFilter == label

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (selected) Color(0xFFFF9800) else Color.Transparent
                            )
                            .clickable {
                                if (!selected) onFilterChange(label)
                            }
                         ,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (selected) Color.White else Color.Black,
                            fontSize = 13.sp
                        )
                    }

                    if (index < items.lastIndex) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Divider(
                            modifier = Modifier
                                .height(30.dp)
                                .width(1.dp),
                            color = Color(0xFFE0E0E0)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun HorizontalCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()

    // Tạo danh sách với today ở giữa
    val allDates = rememberSaveable {
        val beforeToday = generateSequence(today.minusDays(1)) { it.minusDays(1) }
            .takeWhile { it >= today.minusMonths(6) }
            .toList()
            .reversed()

        val afterToday = generateSequence(today.plusDays(1)) { it.plusDays(1) }
            .takeWhile { it <= today.plusMonths(6) }

        (beforeToday + listOf(today) + afterToday).toList()
    }

    // Khởi tạo scroll position ngay tại today
    val scrollState = rememberLazyListState(
        initialFirstVisibleItemIndex = allDates.indexOf(selectedDate).coerceAtLeast(0)
    )

    LazyRow(
        state = scrollState,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        items(allDates) { date ->
            DayItem(
                date = date,
                isSelected = date == selectedDate,
                isToday = date == today,
                onClick = {
                    onDateSelected(date)
                }
            )
        }
    }
}


@Composable
private fun DayItem(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val bg   = if (isSelected) Color(0xFF4CAF81) else Color.White
    val text = if (isSelected) Color.White
    else if (isToday) Color(0xFF4CAF81) else Color.Black
    val border = if (isToday && !isSelected) Color(0xFF4CAF81) else Color.Transparent

    Column(
        Modifier
            .width(56.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, border, RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Th${date.monthValue}", fontSize = 9.sp, color = text, fontWeight = FontWeight.Bold)
        Text(date.dayOfMonth.toString(), fontSize = 16.sp, color = text,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal)

        val dow = arrayOf("CN","Thứ 2","Thứ 3","Thứ 4","Thứ 5","Thứ 6","Thứ 7")[date.dayOfWeek.value % 7]
        Text(dow, fontSize = 12.sp, color = text,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)
@Composable
fun TodoItemCard(
    todo: Todo,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    val formatterTime = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

    val backgroundColor = if (todo.isHighPriority) Color(0xFFFFF3CD) else Color.White
    val borderColor = Color.LightGray
    val alpha = if (todo.isDone) 0.5f else 1f

    var showConfirmDialog by remember { mutableStateOf(false) }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                showConfirmDialog = true
                false // Không tự động xóa khi trượt
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
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                    .background(backgroundColor, RoundedCornerShape(16.dp))
                    .alpha(alpha)
                    .clickable { onClick() }
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onCheckedChange(!todo.isDone) }) {
                            Icon(
                                imageVector = if (todo.isDone) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                                contentDescription = "Hoàn thành"
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Column {
                            Text(
                                text = todo.title,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                text = todo.detail,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = todo.date.format(formatterDate),
                            style = MaterialTheme.typography.bodyMedium,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = todo.startTime.format(formatterTime),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(16.dp),
                                tint = Color.Gray
                            )
                            Text(
                                text = todo.endTime.format(formatterTime),
                                style = MaterialTheme.typography.bodySmall
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
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            text = { Text("Bạn có chắc chắn muốn xóa công việc này không?") }
        )
    }
}

@Composable
fun OverviewCard(
    date: String,
    todos: List<Todo>,
    modifier: Modifier = Modifier
) {
    val totalTasks = todos.size
    val completedTasks = todos.count{it.isDone}

    val progress = if (totalTasks == 0) 0f else completedTasks.toFloat() / totalTasks
    val image = painterResource(R.drawable.cat)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Transparent)
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.cat), // thay bằng ảnh bạn cung cấp
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp))
        )
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bên trái: Text
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(1f).padding(start = 10.dp)
            ) {
                Text("Tổng quan", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(date, color = Color.White)
                Text("$totalTasks nhiệm vụ", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.White)
            }

//            // Ảnh mèo
//            Image(
//                painter = image,
//                contentDescription = "Mèo minh họa",
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 12.dp)
//                    .fillMaxHeight(),
//                contentScale = ContentScale.Fit
//            )

            // Bên phải: Vòng tròn phần trăm
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(end = 10.dp)) {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.size(60.dp),
                    color = Color.White,
                    strokeWidth = 6.dp,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
                Text(
                    text = "${(progress * 100).roundToInt()}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}