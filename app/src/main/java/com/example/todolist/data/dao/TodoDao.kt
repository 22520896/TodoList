package com.example.todolist.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.data.entity.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Query("SELECT * FROM todos ORDER BY startTime ASC")
    fun getAllTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: Long): Todo?

    @Update
    suspend fun update(todo: Todo)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteTodo(id: Long)

    @Query("DELETE FROM todos")
    suspend fun deleteAllTodos()

    // Lọc todo theo ngày (dựa trên trường date)
    @Query("SELECT * FROM todos WHERE strftime('%Y-%m-%d', datetime(date / 1000, 'unixepoch', 'localtime')) = :dateStr ORDER BY startTime ASC")
    fun getTodosByDate(dateStr: String): Flow<List<Todo>>

    // Lọc todo theo tháng (dựa trên trường date)
    @Query("SELECT * FROM todos WHERE strftime('%Y-%m', datetime(date / 1000, 'unixepoch', 'localtime')) = :monthStr ORDER BY startTime ASC")
    fun getTodosByMonth(monthStr: String): Flow<List<Todo>>

    // Lọc todo theo tuần (dựa trên trường date, tuần bắt đầu từ thứ Hai theo ISO)
    @Query("SELECT * FROM todos WHERE strftime('%Y-%W', datetime(date / 1000, 'unixepoch', 'localtime')) = :weekStr ORDER BY startTime ASC")
    fun getTodosByWeek(weekStr: String): Flow<List<Todo>>
}

