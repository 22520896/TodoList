package com.example.todolist.data.repository

import android.content.Context
import com.example.todolist.alarm.ReminderScheduler
import com.example.todolist.data.dao.TodoDao
import com.example.todolist.data.entity.Todo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


// Bọc lại các phương thức của DAO và cung cấp chúng cho ViewModel

//class TodoRepository @Inject constructor(private val todoDao: TodoDao, private val reportRepository: ReportRepository) {
//    suspend fun insertTodo(todo: Todo)  {
//        todoDao.insert(todo)
//        reportRepository.updateReportFor(todo.date)
//    }
//
//    suspend fun getTodoById(id: Long): Todo? = todoDao.getTodoById(id)
//
//    suspend fun updateTodo(todo: Todo)  {
//        todoDao.update(todo)
//        reportRepository.updateReportFor(todo.date)
//    }
//
//    suspend fun deleteTodo(todo: Todo) {
//        todoDao.deleteTodo(todo)
//        if (todo != null) {
//            todoDao.deleteTodo(todo)
//            reportRepository.updateReportFor(todo.date)
//        }
//    }
//
//    fun getTodosByDate(date: LocalDate): Flow<List<Todo>> = todoDao.getTodosByDate(date)
//
//    fun getTodosByMonth(monthStr: String): Flow<List<Todo>> = todoDao.getTodosByMonth(monthStr)
//
//    fun getTodosByWeek(weekStr: String): Flow<List<Todo>> = todoDao.getTodosByWeek(weekStr)
//}

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao,
    private val reportRepository: ReportRepository,
    @ApplicationContext private val context: Context
) {

    suspend fun insertTodo(todo: Todo) {
        val id = todoDao.insert(todo)
        reportRepository.updateReportFor(todo.date)
        if (todo.reminderOffsets.isNotEmpty() || todo.isFocusEnabled) {
            ReminderScheduler.scheduleReminders(context, todo.copy(id = id))
        }
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.update(todo)
        reportRepository.updateReportFor(todo.date)
        ReminderScheduler.cancelReminders(context, todo)
        if (todo.reminderOffsets.isNotEmpty() || todo.isFocusEnabled) {
            ReminderScheduler.scheduleReminders(context, todo)
        }
    }

    suspend fun deleteTodo(todo: Todo) {
        ReminderScheduler.cancelReminders(context, todo)
        todoDao.deleteTodo(todo)
        reportRepository.updateReportFor(todo.date)
    }
    suspend fun getTodoById(id: Long): Todo? = todoDao.getTodoById(id)

    fun getTodosByDate(date: LocalDate): Flow<List<Todo>> = todoDao.getTodosByDate(date)

    fun getTodosByMonth(monthStr: String): Flow<List<Todo>> = todoDao.getTodosByMonth(monthStr)

    fun getTodosByWeek(weekStr: String): Flow<List<Todo>> = todoDao.getTodosByWeek(weekStr)
}
