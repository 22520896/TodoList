package com.example.todolist.ui.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist.ui.CommonSwitch
import com.example.todolist.ui.CommonTitleField
import com.example.todolist.viewmodel.CommonViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.format.TextStyle
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoModal(
    viewModel: TodoModalViewModel = hiltViewModel(),
    formatterTime: DateTimeFormatter,
    color: Color,
    onDismiss: () -> Unit
) {
    val date by viewModel.date.collectAsStateWithLifecycle()
    val startTime by viewModel.startTime.collectAsStateWithLifecycle()
    val endTime by viewModel.endTime.collectAsStateWithLifecycle()
    val reminderOffsets by viewModel.reminderOffsets.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val detail by viewModel.detail.collectAsStateWithLifecycle()
    val isHighPriority by viewModel.isHighPriority.collectAsStateWithLifecycle()
    val isFocusEnabled by viewModel.isFocusEnabled.collectAsStateWithLifecycle()
    val isValid by viewModel.isValid.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val isReminderEnabled by viewModel.isReminderEnabled.collectAsStateWithLifecycle()


    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp)
    ) {
        // Tiêu đề căn giữa
        Text(
            text = "NHIỆM VỤ",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center)
        )
        // Nút Đóng (Start)
        IconButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.Default.Close, null)
        }
        // Nút Lưu (End)
        IconButton(
            onClick = {
                scope.launch {
                    if (viewModel.saveTodo()) {
                        sheetState.hide()
                        onDismiss()
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                Icons.Default.Check, null,
                tint = if (isValid) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline
            )
        }
    }
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 8.dp)
    ) {
        // Tiêu đề và chi tiết
        TextFieldsCard(title, detail, viewModel::updateTitle, viewModel::updateDetail)

        // Thời gian thực hiện
        DateTimeSection(
            date = date,
            startTime = startTime,
            endTime = endTime,
            viewModel = viewModel,
            formatterTime = formatterTime,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
                .shadow(4.dp, RoundedCornerShape(12.dp)) // Bóng nhẹ
                .background(Color.White, RoundedCornerShape(12.dp)) // Nền trắng
                .padding(12.dp)
        ) {
            Column() {
                // Reminder Offsets
                ReminderSettingsBlock(
                    isEnabled = isReminderEnabled,
                    onEnabledChange = { viewModel.toggleReminderEnabled() },
                    reminderOffsets = reminderOffsets,
                    onOffsetsChange = { viewModel.updateReminderOffsets(it) },
                    color = color
                )

                // Checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PriorityHigh,
                            contentDescription = "Ưu tiên",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        CommonTitleField("Ưu tiên", Modifier.padding(end = 8.dp))
                        CommonSwitch(
                            color = color,
                            isCheck = isHighPriority,
                            onCheckedChange = { viewModel.toggleIsHighPriority() }
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timelapse,
                            contentDescription = "Tập trung",
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        CommonTitleField("Tập trung", Modifier.padding(end = 8.dp))
                        CommonSwitch(
                            color = color,
                            isCheck = isFocusEnabled,
                            onCheckedChange = { viewModel.toggleIsFocusEnabled() }
                        )
                    }
                }
            }
        }
    }

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            },
            title = { Text("Lỗi") },
            text = { Text(errorMessage!!) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldsCard(title: String, detail: String, onTitle: (String) -> Unit, onDetail: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
            .shadow(3.dp, RoundedCornerShape(14.dp)),
        shape  = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = onTitle,
                    placeholder = { Text("Tiêu đề", style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold)) },
                    singleLine = true,
                    textStyle     = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = detail,
                    onValueChange =  onDetail,
                    placeholder = { Text("Thêm chi tiết") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()

                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeSection(
    date: LocalDate,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    viewModel: TodoModalViewModel,
    formatterTime: DateTimeFormatter,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    val startTimePickerState = rememberTimePickerState(
        initialHour = startTime.hour,
        initialMinute = startTime.minute
    )
    val endTimePickerState = rememberTimePickerState(
        initialHour = endTime.hour,
        initialMinute = endTime.minute
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .shadow(3.dp, RoundedCornerShape(12.dp)) // Bóng nhẹ
            .background(Color.White, RoundedCornerShape(12.dp)) // Nền trắng
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            // Tiêu đề
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 2.dp)) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Thời gian thực hiện",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.padding(end = 4.dp)
                )
                CommonTitleField("Thời gian thực hiện")
            }

            Spacer(Modifier.height(8.dp))

            // Ngày
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = { showDatePicker = true },
                ) {
                    val dateText = "${date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("vi", "VN")).replaceFirstChar { it.uppercase() }}, ${date.dayOfMonth} tháng ${date.monthValue} năm ${date.year}"
                    Text(dateText, fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(4.dp))

            // Giờ bắt đầu & kết thúc
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically, // Căn giữa theo chiều dọc
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Giờ bắt đầu", style = MaterialTheme.typography.bodySmall)
                    TextButton(
                        onClick = { showStartTimePicker = true },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(startTime.format(formatterTime), fontSize = 16.sp)
                    }
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp) // Kích thước mũi tên hợp lý
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Giờ kết thúc", style = MaterialTheme.typography.bodySmall)
                    TextButton(
                        onClick = { showEndTimePicker = true },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(endTime.format(formatterTime), fontSize = 16.sp)
                    }
                }
            }
        }
    }

    // Dialogs
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val newDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        viewModel.updateDate(newDate)
                        viewModel.updateStartTime(
                            LocalDateTime.of(newDate, startTime.toLocalTime())
                        )
                        viewModel.updateEndTime(
                            LocalDateTime.of(newDate, endTime.toLocalTime())
                        )
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Hủy") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showStartTimePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val newStartTime = LocalDateTime.of(
                        date,
                        LocalTime.of(
                            startTimePickerState.hour,
                            startTimePickerState.minute
                        )
                    )
                    viewModel.updateStartTime(
                        newStartTime
                    )
                    showStartTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartTimePicker = false }) { Text("Hủy") }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                TimePicker(state = startTimePickerState)
            }
        }
    }

    if (showEndTimePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val newEndTime = LocalDateTime.of(
                        date,
                        LocalTime.of(
                            endTimePickerState.hour,
                            endTimePickerState.minute
                        )
                    )
                    viewModel.updateEndTime(
                        newEndTime
                    )
                    showEndTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndTimePicker = false }) { Text("Hủy") }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                TimePicker(state = endTimePickerState)
            }
        }
    }
}

//Modal lựa chọn
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderPickerSheet(
    selectedOffsets: List<Long>,
    onDismiss: () -> Unit,
    onConfirm: (List<Long>) -> Unit
) {
    val allOptions = listOf(
        5L * 60_000L to "5 phút trước",
        15L * 60_000L to "15 phút trước",
        30L * 60_000L to "30 phút trước",
        60L * 60_000L to "1 giờ trước",
        120L * 60_000L to "2 giờ trước"
    )
    val availableOptions = allOptions.filterNot { it.first in selectedOffsets }

    var tempSelection by remember { mutableStateOf<Long?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = {}
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            // Title Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Hủy bỏ")
                }
                Text(
                    "Chọn nhắc nhở lúc",
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = {
                    tempSelection?.let {
                        onConfirm(selectedOffsets + it)
                    }
                    onDismiss()
                }) {
                    Text("Hoàn tất")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // List
            availableOptions.forEach { (offset, label) ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { tempSelection = offset }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = label)
                    if (tempSelection == offset) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            if (availableOptions.isEmpty()) {
                Text("Tất cả lựa chọn đã được chọn", modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}


@Composable
fun ReminderSettingsBlock(
    color: Color,
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    reminderOffsets: List<Long>,
    onOffsetsChange: (List<Long>) -> Unit,
) {
    var showAddReminderSheet by remember { mutableStateOf(false) }

    //Header
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row() {
            Icon(
                Icons.Default.NotificationsActive,
                contentDescription = null,
//                tint = Color(0xFF4CAF50),
                modifier = Modifier.padding(end = 4.dp)
            )
            CommonTitleField("Cài đặt nhắc nhở", Modifier)
        }
        CommonSwitch(color = color, isCheck = isEnabled, onCheckedChange = {
                onEnabledChange(it)
                if (it && reminderOffsets.isEmpty()) {
                    onOffsetsChange(listOf(0L))
                }
        })

    }

    // List reminders
    if (isEnabled) {
        Column(Modifier.padding(start = 40.dp)) {
            reminderOffsets.sorted().forEach { offset ->
                val label = when (offset) {
                    0L -> "Đúng giờ"
                    in 1 until 60 * 60 * 1000 -> "${offset / 60_000} phút trước"
                    else -> "${offset / (60 * 60 * 1000)} giờ trước"
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = null,
//                            tint = Color(0xFF4CAF50)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(label)
                    }

                    if (offset != 0L) {
                        IconButton(onClick = {
                            onOffsetsChange(reminderOffsets - offset)
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Xóa")
                        }
                    }
                }
            }

            //"Thêm nhắc nhở"
            TextButton(
                onClick = { showAddReminderSheet = true },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Thêm nhắc nhở")
            }
        }
    }

    //Modal Sheet
    if (showAddReminderSheet) {
        ReminderPickerSheet(
            selectedOffsets = reminderOffsets,
            onDismiss      = { showAddReminderSheet = false },
            onConfirm      = { newList ->
                val updated = (newList + 0L).distinct()
                onOffsetsChange(updated)
                showAddReminderSheet = false
            }
        )
    }
}
