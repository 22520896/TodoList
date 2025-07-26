package com.example.todolist

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.todolist.alarm.FocusSessionManager
import com.example.todolist.data.entity.Todo
import com.example.todolist.data.repository.TodoRepository
import com.example.todolist.ui.focus.FocusScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FocusActivity : ComponentActivity() {
    @Inject
    lateinit var todoRepository: TodoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val todoId = intent.getLongExtra("todoId", -1)

        if (FocusSessionManager.isRunning() && FocusSessionManager.currentId() != todoId) {
            finish()
            return
        }

        FocusSessionManager.start(todoId)

        setContent {
            val todo by produceState<Todo?>(null) {
                value = todoRepository.getTodoById(todoId)
            }

            todo?.let {
                FocusScreen(
                    todo = it,
                    onCompleted = { isCompleted ->
                        lifecycleScope.launch {
                            val updated = it.copy(isDone = isCompleted)
                            todoRepository.updateTodo(updated)
                        }
                    },
                    onExit = {
                        FocusSessionManager.stop()
                        finish() }
                )
            }
        }
    }
}

