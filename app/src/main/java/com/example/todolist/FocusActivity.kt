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
                    onExit = { finish() }
                )
            }
        }
    }
}

//@AndroidEntryPoint
//class FocusActivity : ComponentActivity() {
//
//    @Inject
//    lateinit var todoRepository: TodoRepository
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val todoId = intent.getLongExtra("todoId", -1L)
//        Log.e("FocusActivity", "📌 Mở với todoId = $todoId")
//
//        if (todoId == -1L) {
//            Toast.makeText(this, "Không có todoId!", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//
//        setContent {
//            var todo by remember { mutableStateOf<Todo?>(null) }
//
//            LaunchedEffect(Unit) {
//                val loaded = todoRepository.getTodoById(todoId)
//                Log.e("FocusActivity", "✅ Todo loaded: $loaded - todoId = $todoId")
//                todo = loaded
//            }
//
//            if (todo != null) {
//                FocusDebugView(todo!!)
//            } else {
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("Đang tải dữ liệu...", style = MaterialTheme.typography.bodyLarge)
//                }
//            }
//        }
//    }
//}


@Composable
fun FocusDebugView(todo: Todo) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🔍 Todo Focus Test", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("🆔 ID: ${todo.id}")
        Text("📌 Tiêu đề: ${todo.title}")
        Text("⏰ Bắt đầu: ${todo.startTime}")
        Text("⏳ Kết thúc: ${todo.endTime}")
    }
}