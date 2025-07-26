package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entity.Event
import com.example.todolist.data.entity.Todo
import com.example.todolist.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import java.time.temporal.IsoFields



@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow("Ngày")
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    // Tự động cập nhật todos khi filter hoặc date thay đổi
    val todos = combine(_selectedFilter, _selectedDate) { filter, date ->
        filter to date
    }.flatMapLatest { (filter, date) ->
        when (filter) {
            "Ngày" -> repository.getTodosByDate(date)
            "Tuần" -> repository.getTodosByWeek(formatWeek(date))
            "Tháng" -> repository.getTodosByMonth(formatMonth(date))
            else -> flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }

    fun setDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }

    private fun formatWeek(date: LocalDate): String {
        return "%d-%02d".format(
            date.get(IsoFields.WEEK_BASED_YEAR),
            (date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) - 1).coerceAtLeast(0)
        )
    }

    private fun formatMonth(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM"))
    }
}
