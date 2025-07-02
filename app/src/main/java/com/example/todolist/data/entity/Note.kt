package com.example.todolist.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val updatedAt: LocalDate = LocalDate.now(),
    val title: String = "",
    val content: String = "",
    val isFavourite: Boolean = false
)