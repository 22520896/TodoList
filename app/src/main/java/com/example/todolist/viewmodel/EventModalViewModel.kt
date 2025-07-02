package com.example.todolist.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entity.Event
import com.example.todolist.data.entity.Todo
import com.example.todolist.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class EventModalViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {
    private val _isEditMode = MutableStateFlow(false)
    val   isEditMode = _isEditMode.asStateFlow()

    private val _id = MutableStateFlow(0L)
    val   id = _id.asStateFlow()

    private val _title = MutableStateFlow("")
    val   title = _title.asStateFlow()

    private val _detail = MutableStateFlow("")
    val   detail = _detail.asStateFlow()

    private val _startDate  = MutableStateFlow(LocalDate.now())
    val   startDate = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow(LocalDate.now())
    val   endDate = _endDate.asStateFlow()

    private val _startTime = MutableStateFlow(LocalDateTime.now())
    val   startTime = _startTime.asStateFlow()

    private val _endTime = MutableStateFlow(LocalDateTime.now().plusHours(1))
    val   endTime  = _endTime.asStateFlow()

    private val _reminderOffsets  = MutableStateFlow<List<Long>>(emptyList())
    val   reminderOffsets = _reminderOffsets.asStateFlow()

    private val _isReminderEnabled = MutableStateFlow(false)
    val   isReminderEnabled = _isReminderEnabled.asStateFlow()

    private val _isValid = MutableStateFlow(true)
    val   isValid = _isValid.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val   errorMessage = _errorMessage.asStateFlow()


    /** Khởi tạo dialog thêm mới */
    fun startAddEvent(date: LocalDate = LocalDate.now()) {
        _isEditMode.value       = false
        _id.value               = 0L
        _title.value            = ""
        _detail.value           = ""
        _startDate.value        = date
        _endDate.value          = date

        val now                 = LocalDateTime.now()
        _startTime.value    = now
        _endTime.value      = now.plusHours(1)

        _reminderOffsets.value  = emptyList()
        _isReminderEnabled.value= false

        _isValid.value          = true
        _errorMessage.value     = null
    }

    /** Khởi tạo dialog chỉnh sửa */
    fun startEditEvent(event: Event) {
        _isEditMode.value       = true
        _id.value               = event.id

        _title.value            = event.title
        _detail.value           = event.detail

        _startDate.value        = event.startDate
        _endDate.value          = event.endDate
        _startTime.value    = event.startTime
        _endTime.value      = event.endTime

        _reminderOffsets.value  = event.reminderOffsets
        _isReminderEnabled.value= event.reminderOffsets.isNotEmpty()

        _isValid.value          = true
        _errorMessage.value     = null
    }


    fun updateTitle(value: String)             { _title.value = value }
    fun updateDetail(value: String)            { _detail.value = value }

    fun updateStartDate(value: LocalDate)      { _startDate.value = value }
    fun updateEndDate(value: LocalDate)        { _endDate.value = value }

    fun updateStartTime(value: LocalDateTime){ _startTime.value = value }
    fun updateEndTime(value: LocalDateTime){ _endTime.value   = value }

    fun updateReminderOffsets(list: List<Long>){ _reminderOffsets.value = list }

    fun toggleReminderEnabled()                { _isReminderEnabled.value = !_isReminderEnabled.value }

    /* ---------- Persist ---------- */
    fun saveEvent(): Boolean {
        if (!validate()) return false

        viewModelScope.launch {
            val reminders = if (_isReminderEnabled.value) _reminderOffsets.value else emptyList()

            val event = Event(
                id            = if (_isEditMode.value) _id.value else 0L,
                title         = _title.value.trim(),
                detail        = _detail.value.trim(),
                startDate     = _startDate.value,
                startTime     = _startTime.value,
                endDate       = _endDate.value,
                endTime       = _endTime.value,
                reminderOffsets = reminders
            )

            if (_isEditMode.value) {
                repository.update(event)
            } else {
                repository.insert(event)
            }
            resetState()
        }
        return true
    }


    private fun validate(): Boolean {
        val titleOk      = _title.value.isNotBlank()
        val dateTimeOk   = _startTime.value.isBefore(_endTime.value)

        _isValid.value   = titleOk && dateTimeOk
        _errorMessage.value = when {
            !titleOk    -> "Tiêu đề không được để trống"
            !dateTimeOk -> "Thời gian kết thúc phải sau thời gian bắt đầu"
            else        -> null
        }
        return _isValid.value
    }

    private fun resetState() { startAddEvent(LocalDate.now()) }
    fun clearError()                         { _errorMessage.value = null }
    fun setReminderEnabled(enabled: Boolean) { _isReminderEnabled.value = enabled }
}