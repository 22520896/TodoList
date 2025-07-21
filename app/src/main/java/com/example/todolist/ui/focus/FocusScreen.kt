//package com.example.todolist.ui.focus
//
//import androidx.activity.compose.BackHandler
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.IconButton
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Done
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.RadioButton
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableLongStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberUpdatedState
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.todolist.data.entity.Todo
//import kotlinx.coroutines.delay
//import java.time.Duration
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//
////@Composable
////fun FocusScreen(todo: Todo, onCompleted: (Boolean) -> Unit, onExit: () -> Unit) {
////    var showDialog by rememberSaveable { mutableStateOf(false) }
////
////    val now = rememberUpdatedState(LocalDateTime.now())
////    val remainingSeconds = remember(todo) {
////        mutableStateOf(Duration.between(now.value, todo.endTime).seconds.coerceAtLeast(0))
////    }
////
////    // Countdown Timer
////    LaunchedEffect(todo.id) {
////        while (remainingSeconds.value > 0) {
////            delay(1000)
////            remainingSeconds.value -= 1
////        }
////    }
////
////    Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .padding(24.dp),
////        horizontalAlignment = Alignment.CenterHorizontally,
////        verticalArrangement = Arrangement.Top
////    ) {
////        // Task name & close button
////        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
////            Text(text = todo.title, style = MaterialTheme.typography.titleLarge)
////            IconButton(onClick = { showDialog = true }) {
////                Icon(Icons.Default.Done, contentDescription = "Kết thúc")
////            }
////        }
////
////        Spacer(modifier = Modifier.height(32.dp))
////
////        // Clock display
//////        ClockView()
////        Text(
////            text = formatTime(remainingSeconds.value),
////            fontSize = 24.sp,
////            modifier = Modifier.padding(top = 16.dp)
////        )
////
////        if (showDialog) {
////            ConfirmDialog(
////                onDismiss = { showDialog = false },
////                onConfirm = { isCompleted ->
////                    onCompleted(isCompleted)
////                    onExit()
////                }
////            )
////        }
////    }
////}
////
////fun formatTime(seconds: Long): String {
////    val minutes = seconds / 60
////    val sec = seconds % 60
////    return "%02d:%02d".format(minutes, sec)
////}
////
////@Composable
////fun ConfirmDialog(onDismiss: () -> Unit, onConfirm: (Boolean) -> Unit) {
////    var selected by remember { mutableStateOf<Boolean?>(null) }
////
////    AlertDialog(
////        onDismissRequest = onDismiss,
////        confirmButton = {
////            TextButton(onClick = {
////                selected?.let { onConfirm(it) }
////            }) {
////                Text("Xác nhận")
////            }
////        },
////        dismissButton = {
////            TextButton(onClick = onDismiss) { Text("Hủy") }
////        },
////        title = { Text("Dừng chế độ tập trung") },
////        text = {
////            Column {
////                Text("Bạn đã hoàn thành nhiệm vụ?")
////                Row(verticalAlignment = Alignment.CenterVertically) {
////                    RadioButton(selected = selected == true, onClick = { selected = true })
////                    Text("Đã hoàn thành")
////                }
////                Row(verticalAlignment = Alignment.CenterVertically) {
////                    RadioButton(selected = selected == false, onClick = { selected = false })
////                    Text("Chưa hoàn thành")
////                }
////            }
////        }
////    )
////}
//
//@Composable
//fun FocusScreen(
//    todo: Todo,
//    onCompleted: (Boolean) -> Unit,
//    onExit: () -> Unit
//) {
//    var showDialog by rememberSaveable { mutableStateOf(false) }
//    var isTimeout by rememberSaveable { mutableStateOf(false) }
//    var remainingSeconds by remember { mutableLongStateOf(0L) }
//
//    val now = rememberUpdatedState(LocalDateTime.now())
//
//    // Khởi tạo thời gian còn lại
//    LaunchedEffect(todo.id) {
//        remainingSeconds = Duration.between(now.value, todo.endTime)
//            .seconds
//            .coerceAtLeast(0)
//    }
//
//    // Đếm ngược mỗi giây
//    LaunchedEffect(todo.id) {
//        while (remainingSeconds > 0) {
//            delay(1000)
//            remainingSeconds--
//        }
//        if (remainingSeconds <= 0) {
//            isTimeout = true
//            showDialog = true
//        }
//    }
//
//    BackHandler {
//        isTimeout = false
//        showDialog = true
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
//    ) {
//        Text(
//            "CHẾ ĐỘ TẬP TRUNG",
//            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
//            modifier = Modifier.align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Text(text = todo.title, style = MaterialTheme.typography.titleMedium)
//        Spacer(modifier = Modifier.height(4.dp))
//        Text(text = todo.detail, style = MaterialTheme.typography.bodyMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        val dateStr = todo.startTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//        val startStr = todo.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
//        val endStr = todo.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))
//
//        Text(
//            text = "Thời gian: $dateStr | $startStr → $endStr",
//            style = MaterialTheme.typography.bodySmall
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Text(
//            text = formatTime(remainingSeconds),
//            style = MaterialTheme.typography.displaySmall,
//            fontWeight = FontWeight.SemiBold
//        )
//
//        Box(modifier = Modifier.fillMaxSize()) {
//            Button(
//                onClick = {
//                    isTimeout = false
//                    showDialog = true
//                },
//                modifier = Modifier.align(Alignment.TopEnd)
//            ) {
//
//                Text("Kết thúc")
//            }
//        }
//    }
//
//    if (showDialog) {
//        ConfirmExitDialog(
//            isTimeout = isTimeout,
//            onDismiss = { showDialog = false },
//            onConfirm = { completed ->
//                onCompleted(completed)
//                onExit()
//            }
//        )
//    }
//}
//
//@Composable
//fun ConfirmExitDialog(
//    isTimeout: Boolean,
//    onDismiss: () -> Unit,
//    onConfirm: (Boolean) -> Unit
//) {
//    var selected by remember { mutableStateOf<Boolean?>(null) }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = {
//            Text(
//                if (isTimeout)
//                    "Đã hết thời gian tập trung"
//                else
//                    "Chưa hết thời gian tập trung"
//            )
//        },
//        text = {
//            Column {
//                Text("Bạn đã hoàn thành nhiệm vụ?")
//                Spacer(Modifier.height(12.dp))
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    RadioButton(selected == true, onClick = { selected = true })
//                    Text("Đã hoàn thành")
//                }
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    RadioButton(selected == false, onClick = { selected = false })
//                    Text("Chưa hoàn thành")
//                }
//            }
//        },
//        confirmButton = {
//            TextButton(
//                onClick = {
//                    if (selected != null) onConfirm(selected!!)
//                }
//            ) {
//                Text("Xác nhận")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Huỷ")
//            }
//        }
//    )
//}
//
//fun formatTime(seconds: Long): String {
//    val min = seconds / 60
//    val sec = seconds % 60
//    return "%02d:%02d".format(min, sec)
//}
//

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
            painter = painterResource(id = R.drawable.img4), // <-- ảnh nền của bạn
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 📦 Lớp phủ tối nhẹ
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // 📄 Nội dung
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔝 Dòng CHẾ ĐỘ TẬP TRUNG
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

            // 📝 Thông tin nhiệm vụ
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

            // ⏱ Đếm ngược
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

            // 🔚 Nút "Kết thúc"
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

//@Composable
//fun FocusScreen(
//    todo: Todo,
//    onCompleted: (Boolean) -> Unit,
//    onExit: () -> Unit
//) {
//    var showDialog by rememberSaveable { mutableStateOf(false) }
//    var isTimeout by rememberSaveable { mutableStateOf(false) }
//    var remainingSeconds by remember { mutableLongStateOf(0L) }
//
//    val now = rememberUpdatedState(LocalDateTime.now())
//
//    // Khởi tạo thời gian còn lại
//    LaunchedEffect(todo.id) {
//        remainingSeconds = Duration.between(now.value, todo.endTime)
//            .seconds
//            .coerceAtLeast(0)
//    }
//
//    // Đếm ngược mỗi giây
//    LaunchedEffect(todo.id) {
//        while (remainingSeconds > 0) {
//            delay(1000)
//            remainingSeconds--
//        }
//        if (remainingSeconds <= 0) {
//            isTimeout = true
//            showDialog = true
//        }
//    }
//
//    BackHandler {
//        isTimeout = false
//        showDialog = true
//    }
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//            .background(Color.White),
//        contentAlignment = Alignment.Center
//    ){
//        Box(
//            modifier = Modifier
//                .wrapContentSize()
//                .background(Color.White, RoundedCornerShape(16.dp))
//                .padding(0.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
////                modifier = Modifier
////                    .fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center,
//                    modifier = Modifier.padding(vertical = 4.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Timelapse,
//                        contentDescription = "Đếm ngược",
//                        modifier = Modifier
//                            .size(30.dp)
//                            .padding(end = 8.dp),
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                    Text(
//                        text = "CHẾ ĐỘ TẬP TRUNG",
//                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
//                        fontSize = 20.sp,
//                        textAlign = TextAlign.Center
//                    )
//                }
//
//
////                Text(
////                    text = "CHẾ ĐỘ TẬP TRUNG",
////                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
////                    fontSize = 20.sp,
////                    textAlign = TextAlign.Center,
////                    modifier = Modifier.padding(vertical = 4.dp)
////                )
//
////                Spacer(modifier = Modifier.height(8.dp))
//
//                // Divider giữa "CHẾ ĐỘ TẬP TRUNG" và nội dung
//                Divider(
//                    color = Color.Gray.copy(alpha = 0.2f),
//                    thickness = 1.dp,
//                    modifier = Modifier.padding(vertical = 4.dp)
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = todo.title,
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.SemiBold,
//                    textAlign = TextAlign.Center
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = todo.detail,
//                    style = MaterialTheme.typography.bodyLarge,
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                val dateStr = todo.startTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//                val startStr = todo.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
//                val endStr = todo.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))
//                Text(
//                    text = "Thời gian: $dateStr | $startStr → $endStr",
//                    style = MaterialTheme.typography.bodyMedium,
//                    textAlign = TextAlign.Center
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
////                Text(
////                    text = formatTime(remainingSeconds),
////                    style = MaterialTheme.typography.displaySmall,
////                    fontWeight = FontWeight.SemiBold,
////                    fontSize = 32.sp
////                )
////
////                Spacer(modifier = Modifier.height(16.dp))
//
//                // Đồng hồ đếm ngược với CircularProgressIndicator bao quanh
//                Box(
//                    modifier = Modifier.size(160.dp), // Kích thước vòng tròn bao quanh
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(
//                        progress = (remainingSeconds.toFloat() / Duration.between(todo.startTime, todo.endTime).seconds.toFloat()).coerceIn(0f, 1f),
//                        modifier = Modifier.fillMaxSize(),
//                        strokeWidth = 4.dp,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    Text(
//                        text = formatTime(remainingSeconds),
//                        style = MaterialTheme.typography.bodyLarge,
//                        fontWeight = FontWeight.Medium,
//                        fontSize = 32.sp,
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Divider giữa nội dung và nút Kết thúc
//                Divider(
//                    color = Color.Gray.copy(alpha = 0.2f),
//                    thickness = 1.dp,
//                    modifier = Modifier.padding(vertical = 8.dp)
//                )
//
//
//                // Nút Kết thúc dạng TextButton
//                TextButton(
//                    onClick = {
//                        isTimeout = false
//                        showDialog = true
//                    },
//                    modifier = Modifier.align(Alignment.End)
//                ) {
//                    Text(
//                        text = "Kết thúc",
//                        style = MaterialTheme.typography.labelLarge,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                }
//            }
//        }
//    }
//
//    if (showDialog) {
//        ConfirmExitDialog(
//            isTimeout = isTimeout,
//            onDismiss = { showDialog = false },
//            onConfirm = { completed ->
//                onCompleted(completed)
//                onExit()
//            }
//        )
//    }
//}

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