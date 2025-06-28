package com.example.todolist.ui.home.todo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.LocalDate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.viewmodel.DateFilter
//import com.example.todolist.viewmodel.TodoUiState
import com.example.todolist.viewmodel.TodoViewModel
import java.time.format.DateTimeFormatter


@Composable
fun TodoScreen(
    viewModel: TodoViewModel = hiltViewModel()
) {
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val todos by viewModel.todos.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TodoTopNavBar(
                selectedFilter = selectedFilter,
                onFilterChange = { viewModel.setFilter(it) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(visible = selectedFilter == "Ngày") {
                HorizontalCalendar(
                        selectedDate = selectedDate,
                        onDateSelected = { viewModel.setDate(it) },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
            }

            AnimatedVisibility(visible = selectedFilter == "Tuần") {
                Text(
                    text = "Công việc ngày ${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }

            AnimatedVisibility(visible = selectedFilter == "Tháng") {
                Text(
                    text = "Công việc ngày ${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(todos) { todo ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8F8F8))
                            .padding(10.dp)
                    ) {
                        Text(text = "• $todo", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}



//@Composable
//fun DayTodoScreen() {
//    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.now()) }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//
//        HorizontalCalendar(
//            initialSelectedDate = selectedDate,
//            onDateSelected = { date ->
//                selectedDate = date
//            },
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//
//        Text(
//            text = "Công việc ngày ${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
//            modifier = Modifier.padding(16.dp),
//        )
//
//        val fakeTodos = listOf(
//            "Học Compose",
//            "Gửi báo cáo cho sếp",
//            "Tập thể dục",
//            "Đi siêu thị"
//        )
//
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(fakeTodos.size) { todo ->
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(10.dp))
//                        .background(Color(0xFFF8F8F8))
//                        .padding(12.dp)
//                ) {
//                    Text(text = "• ${fakeTodos[todo]}", fontSize = 16.sp)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun DayTodoScreen(
//    ui: TodoUiState,
//    onDateSelected: (LocalDate) -> Unit
//) {
//    Column(Modifier.fillMaxSize()) {
//
//        HorizontalCalendar(
//            selectedDate  = ui.selectedDate,
//            onDateSelected = onDateSelected,
//            modifier = Modifier.padding(vertical = 8.dp)
//        )
//
//        Text(
//            "Công việc ngày ${ui.selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}",
//            Modifier.padding(16.dp)
//        )
//
//        LazyColumn(
//            Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(ui.todosToday) { todo ->
//                Surface(
//                    shape = RoundedCornerShape(10.dp),
//                    color = Color(0xFFF8F8F8),
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("• $todo", fontSize = 16.sp, modifier = Modifier.padding(12.dp))
//                }
//            }
//        }
//    }
//}