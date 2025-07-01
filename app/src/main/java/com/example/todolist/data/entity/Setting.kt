package com.example.todolist.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Setting(
    @PrimaryKey val id: Int = 1,
    val dateFormat: String,
    val timeFormat: String,
    val color: String,
    val ringtoneUri: String,
    val ringtoneName: String
)