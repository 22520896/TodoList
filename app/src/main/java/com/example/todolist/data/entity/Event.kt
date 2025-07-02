package com.example.todolist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val detail: String,
    val startDate: LocalDate,
    val startTime: LocalDateTime,
    val endDate: LocalDate,
    val endTime: LocalDateTime,
    val reminderOffsets: List<Long>
)