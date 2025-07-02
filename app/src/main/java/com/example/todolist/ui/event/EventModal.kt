package com.example.todolist.ui.event

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist.ui.CommonTitleField
import com.example.todolist.ui.todo.ReminderSettingsBlock
import com.example.todolist.ui.todo.TextFieldsCard
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.EventModalViewModel
import kotlinx.coroutines.launch
import java.time.Instant

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventModal(
    viewModel: EventModalViewModel = hiltViewModel(),
    commonViewModel: CommonViewModel,
    onDismiss: () -> Unit
) {
    val title by viewModel.title.collectAsStateWithLifecycle()
    val detail by viewModel.detail.collectAsStateWithLifecycle()
    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val endDate by viewModel.endDate.collectAsStateWithLifecycle()
    val startTime by viewModel.startTime.collectAsStateWithLifecycle()
    val endTime by viewModel.endTime.collectAsStateWithLifecycle()
    val reminderOffsets by viewModel.reminderOffsets.collectAsStateWithLifecycle()
    val isReminderEnabled by viewModel.isReminderEnabled.collectAsStateWithLifecycle()
    val isValid by viewModel.isValid.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val timeFormat by commonViewModel.timeFormat.collectAsStateWithLifecycle()
    val color by commonViewModel.color.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Header
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp)
    ) {
        Text(
            text = "SỰ KIỆN",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center)
        )
        IconButton(onClick = onDismiss, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(Icons.Default.Close, null)
        }
        IconButton(
            onClick = {
                scope.launch {
                    if (viewModel.saveEvent()) {
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
        TextFieldsCard(title, detail, viewModel::updateTitle, viewModel::updateDetail)

        DateTimeSection(
            startDate = startDate,
            endDate = endDate,
            startTime = startTime,
            endTime = endTime,
            viewModel = viewModel,
            timeFormat = timeFormat
        )

        ReminderSettingsBlock(
            color = color,
            isEnabled = isReminderEnabled,
            onEnabledChange = { viewModel.toggleReminderEnabled() },
            reminderOffsets = reminderOffsets,
            onOffsetsChange = { viewModel.updateReminderOffsets(it) }
        )
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
fun DateTimeSection(
    startDate: LocalDate,
    endDate: LocalDate,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    viewModel: EventModalViewModel,
    timeFormat: String
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = startDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = endDate
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

    val timeFormatter = DateTimeFormatter.ofPattern(timeFormat, Locale("vi", "VN"))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column (verticalArrangement = Arrangement.Center){
            // Tiêu đề
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Thời gian thực hiện",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.padding(end = 4.dp)
                )
                CommonTitleField("Thời gian thực hiện")
            }

            Spacer(Modifier.height(8.dp))

            // Giờ bắt đầu & kết thúc
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                    Text("Bắt đầu", style = MaterialTheme.typography.bodySmall)

                    Box(contentAlignment = Alignment.Center) {
                        TextButton(
                            onClick = { showStartDatePicker = true },
                        ) {
                            val dateText = "${startDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale("vi", "VN")).replaceFirstChar { it.uppercase() }}, ${startDate.dayOfMonth} tháng ${startDate.monthValue} năm ${startDate.year}"
                            Text(dateText, fontSize = 16.sp)
                        }
                    }
                    TextButton(
                        onClick = { showStartTimePicker = true },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(startTime.format(timeFormatter), fontSize = 16.sp)
                    }
                }

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp)
                )

                Column (verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Kết thúc", style = MaterialTheme.typography.bodySmall)
                    Box(contentAlignment = Alignment.Center) {
                        TextButton(
                            onClick = { showEndDatePicker = true },
                        ) {
                            val dateText = "${startDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale("vi", "VN")).replaceFirstChar { it.uppercase() }}, ${endDate.dayOfMonth} tháng ${endDate.monthValue} năm ${endDate.year}"
                            Text(dateText, fontSize = 16.sp)
                        }
                    }
                    TextButton(
                        onClick = { showEndTimePicker = true },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(endTime.format(timeFormatter), fontSize = 16.sp)
                    }
                }
            }
        }
    }

    // Dialogs
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startDatePickerState.selectedDateMillis?.let { millis ->
                        val newDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        viewModel.updateStartDate(newDate)
                    }
                    showStartDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("Hủy") }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }


    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endDatePickerState.selectedDateMillis?.let { millis ->
                        val newDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        viewModel.updateEndDate(newDate)
                    }
                    showEndDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("Hủy") }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }


    if (showStartTimePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val newStartTime = LocalDateTime.of(
                        startDate,
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
                        endDate,
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