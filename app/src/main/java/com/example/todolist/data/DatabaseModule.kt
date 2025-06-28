package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.todolist.data.AppDatabase
import com.example.todolist.data.dao.TodoDao
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
            "db"                       // tên DB giống bản cũ
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTodoDao(db: AppDatabase): TodoDao =
        db.todoDao()
}
