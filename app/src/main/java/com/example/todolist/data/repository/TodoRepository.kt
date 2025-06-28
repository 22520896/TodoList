package com.example.todolist.data.repository

import com.example.todolist.data.dao.TodoDao
import com.example.todolist.data.entity.Todo
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow


// Bọc lại các phương thức của DAO và cung cấp chúng cho ViewModel

class TodoRepository @Inject constructor(private val todoDao: TodoDao) {
    suspend fun insertTodo(todo: Todo) = todoDao.insert(todo)

    fun getAllTodos(): Flow<List<Todo>> = todoDao.getAllTodos()

    suspend fun getTodoById(id: Long): Todo? = todoDao.getTodoById(id)

    suspend fun updateTodo(todo: Todo) = todoDao.update(todo)

    suspend fun deleteTodo(id: Long) = todoDao.deleteTodo(id)

    suspend fun deleteAllTodos() = todoDao.deleteAllTodos()

    fun getTodosByDate(dateStr: String): Flow<List<Todo>> = todoDao.getTodosByDate(dateStr)

    fun getTodosByMonth(monthStr: String): Flow<List<Todo>> = todoDao.getTodosByMonth(monthStr)

    fun getTodosByWeek(weekStr: String): Flow<List<Todo>> = todoDao.getTodosByWeek(weekStr)
}