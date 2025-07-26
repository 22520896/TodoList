package com.example.todolist.ui.focus

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.data.entity.Todo
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.todolist.R


@Composable
fun FocusScreen(
    todo: Todo,
    onCompleted: (Boolean) -> Unit,
    onExit: () -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var isTimeout by rememberSaveable { mutableStateOf(false) }
    var remainingSeconds by remember { mutableLongStateOf(0L) }

    val now = rememberUpdatedState(LocalDateTime.now())

    LaunchedEffect(todo.id) {
        remainingSeconds = Duration.between(now.value, todo.endTime).seconds.coerceAtLeast(0)
    }

    LaunchedEffect(todo.id) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }
        if (remainingSeconds <= 0) {
            isTimeout = true
            showDialog = true
        }
    }

    BackHandler {
        isTimeout = false
        showDialog = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 📷 Ảnh nền
        Image(
            painter = painterResource(id = R.drawable.img4),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // Nội dung
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dòng CHẾ ĐỘ TẬP TRUNG
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Timelapse,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CHẾ ĐỘ TẬP TRUNG",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Thông tin nhiệm vụ
            Text(
                text = todo.title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = todo.detail,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            val dateStr = todo.startTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val startStr = todo.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
            val endStr = todo.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))

            Text(
                text = "Thời gian: $dateStr | $startStr → $endStr",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Đếm ngược
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = (remainingSeconds.toFloat() /
                            Duration.between(todo.startTime, todo.endTime).seconds.toFloat()).coerceIn(0f, 1f),
                    strokeWidth = 5.dp,
                    color = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = formatTime(remainingSeconds),
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Nút "Kết thúc"
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(
                    onClick = {
                        isTimeout = false
                        showDialog = true
                    }
                ) {
                    Text(
                        text = "Kết thúc",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }

    if (showDialog) {
        ConfirmExitDialog(
            isTimeout = isTimeout,
            onDismiss = { showDialog = false },
            onConfirm = {
                onCompleted(it)
                onExit()
            }
        )
    }
}


@Composable
fun ConfirmExitDialog(
    isTimeout: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Boolean) -> Unit
) {
    var selected by remember { mutableStateOf<Boolean?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isTimeout) "Đã hết thời gian tập trung" else "Chưa hết thời gian tập trung",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column {
                Text(
                    text = "Bạn đã hoàn thành nhiệm vụ?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selected == true, onClick = { selected = true })
                    Text("Đã hoàn thành")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selected == false, onClick = { selected = false })
                    Text("Chưa hoàn thành")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { selected?.let { onConfirm(it) } },
                enabled = selected != null
            ) {
                Text("Xác nhận")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

fun formatTime(seconds: Long): String {
    val hours = seconds / 3600
    val min = (seconds % 3600) / 60
    val sec = seconds % 60
    return "%02d:%02d:%02d".format(hours, min, sec)
}