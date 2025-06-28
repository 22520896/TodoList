package com.example.todolist.data
// Định nghĩa các @TypeConverter, là các phương thức giúp Room ánh xạ các kiểu dữ liệu không được hỗ trợ trực tiếp (như LocalDateTime, List<Long>) sang các kiểu dữ liệu cơ bản mà SQLite hỗ trợ
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converters {
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): Long? =
        dateTime?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    @TypeConverter
    fun toLocalDateTime(timestamp: Long?): LocalDateTime? =
        timestamp?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault()) }

    @TypeConverter
    fun fromReminderOffsetsList(value: List<Long>?): String? =
        value?.joinToString(",")

    @TypeConverter
    fun toReminderOffsetsList(value: String?): List<Long>? =
        value?.split(",")?.mapNotNull { it.toLongOrNull() }
}