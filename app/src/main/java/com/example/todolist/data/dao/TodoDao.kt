package com.example.todolist.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.data.entity.Todo
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo): Long

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Long): Todo?

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    // Lọc todo theo ngày (dựa trên trường date)
    @Query("SELECT * FROM todos WHERE date = :date ORDER BY isHighPriority DESC, startTime ASC")
    fun getTodosByDate(date: LocalDate): Flow<List<Todo>>

    // Lọc todo theo tháng (dựa trên trường date)
    @Query("SELECT * FROM todos WHERE strftime('%Y-%m', date) = :monthStr ORDER BY isHighPriority DESC, date ASC")
    fun getTodosByMonth(monthStr: String): Flow<List<Todo>>

    // Lọc todo theo tuần (dựa trên trường date, tuần bắt đầu từ thứ Hai theo ISO)
    @Query("SELECT * FROM todos WHERE strftime('%Y-%W', date) = :weekStr ORDER BY isHighPriority DESC,date ASC")
    fun getTodosByWeek(weekStr: String): Flow<List<Todo>>

    @Query("SELECT * FROM todos WHERE strftime('%Y-%m', date) = :monthStr")
    suspend fun getTodosByMonthOnce(monthStr: String): List<Todo>
}

