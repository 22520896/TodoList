package com.example.todolist.ui.home.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.viewmodel.DateFilter
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.max

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
//    var selectedDate by rememberSaveable { mutableStateOf(initialSelectedDate) }

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
//        initialFirstVisibleItemIndex = max(0, allDates.indexOf(today) - 3)
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


