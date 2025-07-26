package com.example.todolist.ui.todo


import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.TodoViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.temporal.IsoFields


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel = hiltViewModel(),
    commonViewModel: CommonViewModel = hiltViewModel(),
    formatterDate: DateTimeFormatter,
    formatterTime: DateTimeFormatter,
    bgColor: Color,
    modifier: Modifier
) {
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val todos by viewModel.todos.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val todoModalViewModel: TodoModalViewModel = hiltViewModel()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var isModalVisible by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
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
                .background(Color.White)
        ) {
            when (selectedFilter) {
                "Ngày" -> {
                    Column() {
                        HorizontalCalendar(
                            color = bgColor,
                            selectedDate = selectedDate,
                            onDateSelected = {
                                viewModel.setDate(it)
                                commonViewModel.updateInitDate(it)
                            },
                            modifier = Modifier.padding(vertical = 0.dp)
                        )
                        OverviewCard(
                            date = selectedDate.format(formatterDate),
                            todos = todos
                        )
                    }
                }

                "Tuần" -> {
                    Column() {
                        HorizontalWeekCalendar(
                            color = bgColor,
                            selectedDate = selectedDate,
                            onWeekSelected = { newDate ->
                                viewModel.setDate(newDate)
                                commonViewModel.updateInitDate(newDate)
                            },
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        OverviewCard(
                            date = "Tuần ${selectedDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)} - ${selectedDate.year}",
                            todos = todos
                        )
                    }
                }

                "Tháng" -> {
                    Column() {
                        HorizontalMonthCalendar(
                            color = bgColor,
                            selectedDate = selectedDate,
                            onMonthSelected = { newDate ->
                                viewModel.setDate(newDate)
                                commonViewModel.updateInitDate(newDate)
                            },
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        OverviewCard(
                            date = "Tháng ${selectedDate.monthValue} - ${selectedDate.year}",
                            todos = todos
                        )
                    }
                }
            }

            LazyColumn {
                items(todos, key = { it.id }) { todo ->
                    TodoItemCard(
                        formatterDate = formatterDate,
                        formatterTime = formatterTime,
                        color = bgColor,
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
                            viewModel.deleteTodo(todo)
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
                        formatterTime = formatterTime,
                        color = bgColor,
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
}

