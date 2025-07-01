package com.example.todolist.ui.focus

import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.data.entity.Todo
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun FocusScreen(todo: Todo, onCompleted: (Boolean) -> Unit, onExit: () -> Unit) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val now = rememberUpdatedState(LocalDateTime.now())
    val remainingSeconds = remember(todo) {
        mutableStateOf(Duration.between(now.value, todo.endTime).seconds.coerceAtLeast(0))
    }

    // Countdown Timer
    LaunchedEffect(todo.id) {
        while (remainingSeconds.value > 0) {
            delay(1000)
            remainingSeconds.value -= 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Task name & close button
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = todo.title, style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Done, contentDescription = "Kết thúc")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Clock display
//        ClockView()
        Text(
            text = formatTime(remainingSeconds.value),
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 16.dp)
        )

        if (showDialog) {
            ConfirmDialog(
                onDismiss = { showDialog = false },
                onConfirm = { isCompleted ->
                    onCompleted(isCompleted)
                    onExit()
                }
            )
        }
    }
}

fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val sec = seconds % 60
    return "%02d:%02d".format(minutes, sec)
}

@Composable
fun ConfirmDialog(onDismiss: () -> Unit, onConfirm: (Boolean) -> Unit) {
    var selected by remember { mutableStateOf<Boolean?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                selected?.let { onConfirm(it) }
            }) {
                Text("Xác nhận")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Hủy") }
        },
        title = { Text("Dừng chế độ tập trung") },
        text = {
            Column {
                Text("Bạn đã hoàn thành nhiệm vụ?")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selected == true, onClick = { selected = true })
                    Text("Đã hoàn thành")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selected == false, onClick = { selected = false })
                    Text("Chưa hoàn thành")
                }
            }
        }
    )
}