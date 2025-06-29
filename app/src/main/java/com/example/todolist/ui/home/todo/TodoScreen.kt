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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.todolist.ui.todo.TodoModal
import com.example.todolist.ui.todo.TodoModalViewModel
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.DateFilter
//import com.example.todolist.viewmodel.TodoUiState
import com.example.todolist.viewmodel.TodoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun TodoScreen(
    viewModel: TodoViewModel = hiltViewModel(),
    commonViewModel: CommonViewModel = hiltViewModel()
) {
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

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
                DayTodoScreen(viewModel, commonViewModel)
            }

            AnimatedVisibility(visible = selectedFilter == "Tuần") {
                Text(
                    text = "Công việc ngày o}",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }

            AnimatedVisibility(visible = selectedFilter == "Tháng") {
                Text(
                    text = "Công việc ngày 0",
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayTodoScreen(viewModel: TodoViewModel, commonViewModel: CommonViewModel) {
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    val totalTasks by viewModel.totalTasks.collectAsStateWithLifecycle()
    val completedTasks by viewModel.completedTasks.collectAsStateWithLifecycle()
    val todoModalViewModel: TodoModalViewModel = hiltViewModel()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var isModalVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(selectedDate) {
        viewModel.loadTodos()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalCalendar(
            selectedDate = selectedDate,
            onDateSelected = { viewModel.setDate(it)
                commonViewModel.updateInitDate(it)},
            modifier = Modifier.padding(vertical = 8.dp)
        )
        OverviewCard(
            date = selectedDate.toString(),
            todos = todos
        )
        LazyColumn {
            items(todos) { todo ->
                TodoItemCard(
                    todo = todo,
                    onCheckedChange = { checked ->
                        viewModel.updateTodo(todo.copy(isDone = checked))
                    },
                    onClick = {
                        todoModalViewModel.startEditTodo(todo)
                        isModalVisible = true
                        scope.launch { sheetState.show() }
                    },
                    onDeleteConfirmed = {
                        viewModel.deleteTodo(todo.id)
                    }
                )
            }
        }
        if (isModalVisible) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        isModalVisible = false
                    }
                },
                dragHandle = {}
            ) {
                TodoModal(
                    viewModel = todoModalViewModel,
                    onDismiss = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            isModalVisible = false
                        }
                    }
                )
            }
        }
    }
}
