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
import javax.inject.Inject


enum class DateFilter(val label: String) {
    DAY("Ngày"), WEEK("Tuần"), MONTH("Tháng")
}
@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository): ViewModel() {

    private val _selectedFilter = MutableStateFlow("Ngày")
    val selectedFilter = _selectedFilter.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos = _todos.asStateFlow()

    private val _totalTasks = MutableStateFlow(0)
    val totalTasks = _totalTasks.asStateFlow()

    private val _completedTasks = MutableStateFlow(0)
    val completedTasks  = _completedTasks.asStateFlow()

//    private val _todosByDate = MutableStateFlow<List<Todo>>(emptyList())
//    val todosByDate = _todosByDate.asStateFlow()

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
        loadTodos()
    }

    fun setDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun loadTodos() {
        when (_selectedFilter.value) {
            "Ngày"  -> loadTodosByDate(_selectedDate.value)
            "Tuần"  -> loadTodosByWeek(_selectedDate.value)
            "Tháng" -> loadTodosByMonth(_selectedDate.value)
        }
    }

    fun loadTodosByWeek(value: LocalDate?) {
    }
    fun loadTodosByMonth(value: LocalDate?) {

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
        loadTodos()
    }
    fun deleteTodo(id: Long) {
        viewModelScope.launch {
            repository.deleteTodo(id)
        }
        loadTodos()
    }

    init {
        loadTodos()
    }
}
//    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
//    val todos: StateFlow<List<Todo>> get() = _todos
//


//    private val _todosByMonth = MutableStateFlow<List<Todo>>(emptyList())
//    val todosByMonth: StateFlow<List<Todo>> get() = _todosByMonth
//
//    private val _todosByWeek = MutableStateFlow<List<Todo>>(emptyList())
//    val todosByWeek: StateFlow<List<Todo>> get() = _todosByWeek
//
//    fun loadTodos() {
//        viewModelScope.launch {
//            repository.getAllTodos().collect { todos ->
//                _todos.value = todos
//            }
//        }
//    }
//

//
//    fun loadTodosByMonth(monthStr: String) {
//        viewModelScope.launch {
//            repository.getTodosByMonth(monthStr).collect { todos ->
//                _todosByMonth.value = todos
//            }
//        }
//    }
//
//    fun loadTodosByWeek(weekStr: String) {
//        viewModelScope.launch {
//            repository.getTodosByWeek(weekStr).collect { todos ->
//                _todosByWeek.value = todos
//            }
//        }
//    }
//
//    fun addTodo(todo: Todo) {
//        viewModelScope.launch {
//            repository.insert(todo)
//            loadTodos()
//        }
//    }
//
//    fun updateTodo(todo: Todo) {
//        viewModelScope.launch {
//            repository.update(todo)
//            loadTodos()
//        }
//    }
//
//    fun deleteTodo(id: Long) {
//        viewModelScope.launch {
//            repository.deleteTodo(id)
//            loadTodos()
//        }
//    }
//
//    fun deleteAllTodos() {
//        viewModelScope.launch {
//            repository.deleteAllTodos()
//          