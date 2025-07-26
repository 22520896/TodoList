package com.example.todolist.alarm

object FocusSessionManager {
    @Volatile private var currentTodoId: Long = -1L

    fun isRunning() = currentTodoId != -1L
    fun currentId() = currentTodoId

    fun start(todoId: Long) { currentTodoId = todoId }
    fun stop() { currentTodoId = -1L }
}