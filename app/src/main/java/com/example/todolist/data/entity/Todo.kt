package com.example.todolist.data.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDateTime,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val reminderOffsets: List<Long>,
    val title: String,
    val detail: String,
    val isDone: Boolean = false,
    val isHighPriority: Boolean = false,
    val isFocusEnabled: Boolean = false,
)