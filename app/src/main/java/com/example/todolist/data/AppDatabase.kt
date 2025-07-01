package com.example.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todolist.data.dao.ReportDao
import com.example.todolist.data.dao.SettingDao
import com.example.todolist.data.dao.TodoDao
import com.example.todolist.data.entity.Report
import com.example.todolist.data.entity.Setting
import com.example.todolist.data.entity.Todo

// Định nghĩa cơ sở dữ liệu Room và đăng ký Converters

@Database(entities = [Todo::class, Setting::class, Report::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun settingDao(): SettingDao
    abstract fun reportDao(): ReportDao
}