//package com.example.todolist.di
//
//import android.content.Context
//import androidx.room.Room
//import com.example.todolist.data.AppDatabase
//import com.example.todolist.data.dao.TodoDao
//import com.example.todolist.data.repository.TodoRepository
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
//        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "db")
//            .fallbackToDestructiveMigration()
//            .build()
//
//    @Provides
//    fun provideTodoDao(database: AppDatabase) = database.todoDao()
//
//    @Provides
//    fun provideTodoRepository(todoDao: TodoDao): TodoRepository = TodoRepository(todoDao)
//}