package com.example.todolist.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class Report(
    @PrimaryKey
    val yearMonth: String, // dạng "2025-06"
    val total: Int,
    val completed: Int,
    val incompleted: Int,
    val notStarted: Int
)