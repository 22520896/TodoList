package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.dao.ReportDao
import com.example.todolist.data.dao.SettingDao
import com.example.todolist.data.dao.TodoDao
import com.example.todolist.data.entity.Setting
import com.example.todolist.utils.defaultRingtone
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.Provides
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val (defUri, defName) = defaultRingtone(context)
                    db.execSQL("""
                            INSERT INTO settings (id, dateFormat, timeFormat, color, ringtoneUri, ringtoneName)
                            VALUES (1, 'dd/MM/yyyy', 'HH:mm', '#4CAF81', '$defUri', '$defName')
                        """.trimIndent())
                }
            })
            .build()

    @Provides
    fun provideTodoDao(db: AppDatabase): TodoDao =
        db.todoDao()

    @Provides
    fun provideSettingDao(db: AppDatabase): SettingDao =
        db.settingDao()

    @Provides
    fun provideReportDao(db: AppDatabase): ReportDao =
        db.reportDao()
}
