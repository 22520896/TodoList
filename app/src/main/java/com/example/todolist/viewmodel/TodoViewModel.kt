package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entity.Todo
import com.example.todolist.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import java.time.temporal.IsoFields


//enum class DateFilter(val label: String) {
//    DAY("Ngày"), WEEK("Tuần"), MONTH("Tháng")
//}
@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository): ViewModel() {

    private val _selectedFilter = MutableStateFlow("Ngày")
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos = _todos.asStateFlow()


    fun setFilter(filter: String) {
        _selectedFilter.value = filter
        loadTodos()
    }

    fun setDate(date: LocalDate) {
        _selectedDate.value = date
        loadTodos()
    }

    fun loadTodos() {
        when (_selectedFilter.value) {
            "Ngày"  -> loadTodosByDate(_selectedDate.value)
            "Tuần"  -> loadTodosByWeek(_selectedDate.value)
            "Tháng" -> loadTodosByMonth(_selectedDate.value)
        }
    }

    fun loadTodosByWeek(date: LocalDate) {
        val weekStr = with(date) {
            "%d-%02d".format(
                get(IsoFields.WEEK_BASED_YEAR),
                (get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) - 1).coerceAtLeast(0)
            )
        }
        viewModelScope.launch {
            repository.getTodosByWeek(weekStr).collect { todos ->
                _todos.value = todos
            }
        }
    }

    fun loadTodosByMonth(date: LocalDate) {
        val monthStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM"))
        viewModelScope.launch {
            repository.getTodosByMonth(monthStr).collect { todos ->
                _todos.value = todos
            }
        }
    }

    fun loadTodosByDate(date: LocalDate) {
        viewModelScope.launch {
            repository.getTodosByDate(date).collect { todos ->
                _todos.value = todos
            }
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            repository.updateTodo(todo)
        }
//        loadTodos()
    }


    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
//        loadTodos()
    }

    init {
        loadTodos()
    }
}
