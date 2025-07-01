package com.example.todolist.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entity.Todo
import com.example.todolist.data.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TodoModalViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    // State cho modal (thêm/chỉnh sửa)
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    private val _isReminderEnabled = MutableStateFlow(false)
    val isReminderEnabled = _isReminderEnabled

    // State cho các trường nhập liệu
    private val _date = MutableStateFlow(LocalDate.now())
    val date = _date.asStateFlow()

    private val _startTime = MutableStateFlow(LocalDateTime.now())
    val startTime = _startTime.asStateFlow()

    private val _endTime = MutableStateFlow(LocalDateTime.now().plusHours(1))
    val endTime = _endTime.asStateFlow()

    private val _reminderOffsets = MutableStateFlow<List<Long>>(emptyList())
    val reminderOffsets = _reminderOffsets.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _detail = MutableStateFlow("")
    val detail = _detail.asStateFlow()

    private val _isDone = MutableStateFlow(false)
    val isDone = _isDone.asStateFlow()

    private val _isHighPriority = MutableStateFlow(false)
    val isHighPriority = _isHighPriority.asStateFlow()

    private val _isFocusEnabled = MutableStateFlow(false)
    val isFocusEnabled = _isFocusEnabled.asStateFlow()

    private  val _id = MutableStateFlow(0L)
    val id = _id.asStateFlow()

    // State cho validation
    private val _isValid = MutableStateFlow(true)
    val isValid = _isValid.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Khởi tạo chế độ "thêm"
    fun startAddTodo(date: LocalDate = LocalDate.now()) {
        _isEditMode.value = false
        _isReminderEnabled.value = false
        _date.value = date
        _startTime.value = LocalDateTime.now()
        _endTime.value = LocalDateTime.now().plusHours(1)
        _reminderOffsets.value = emptyList()
        _title.value = ""
        _detail.value = ""
        _isDone.value = false
        _isHighPriority.value = false
        _isFocusEnabled.value = false
        _isValid.value = true
        _errorMessage.value = null
    }

    // Khởi tạo chế độ "chỉnh sửa"
    fun startEditTodo(todo: Todo) {
        _id.value = todo.id
        _isEditMode.value = true
        _date.value = todo.date
        _startTime.value = todo.startTime
        _endTime.value = todo.endTime
        _reminderOffsets.value = todo.reminderOffsets
        _title.value = todo.title
        _detail.value = todo.detail
        _isDone.value = todo.isDone
        _isHighPriority.value = todo.isHighPriority
        _isFocusEnabled.value = todo.isFocusEnabled
        _isValid.value = true
        _errorMessage.value = null
        _isReminderEnabled.value = todo.reminderOffsets.isNotEmpty()
    }

    // Cập nhật các trường

    fun updateDate(date: LocalDate) {
        _date.value = date
//        validateInput()
    }

    fun updateStartTime(startTime: LocalDateTime) {
        _startTime.value = startTime
//        validateInput()
    }

    fun updateEndTime(endTime: LocalDateTime) {
        _endTime.value = endTime
//        validateInput()
    }

    fun updateReminderOffsets(offsets: List<Long>) {
        _reminderOffsets.value = offsets
    }

    fun updateTitle(title: String) {
        _title.value = title
//        validateInput()
    }

    fun updateDetail(detail: String) {
        _detail.value = detail
    }

//    fun toggleIsDone() {
//        _isDone.value = !_isDone.value
//    }

    fun toggleIsHighPriority() {
        _isHighPriority.value = !_isHighPriority.value
    }

    fun toggleIsFocusEnabled() {
        _isFocusEnabled.value = !_isFocusEnabled.value
    }

    fun toggleReminderEnabled (){
        _isReminderEnabled.value = !_isReminderEnabled.value
    }

    fun clearError(){
        _errorMessage.value = null
    }
    // Lưu todo
    fun saveTodo(): Boolean {
        if (validateInput()) {
            viewModelScope.launch {
                val reminders = if (_isReminderEnabled.value == true) {
                    _reminderOffsets.value
                } else {
                    emptyList()
                }
                val todo = Todo(
                    id = if (_isEditMode.value) _id.value else 0,
                    date = _date.value,
                    startTime = _startTime.value,
                    endTime = _endTime.value,
                    reminderOffsets = reminders,
                    title = _title.value,
                    detail = _detail.value,
                    isDone = _isDone.value,
                    isHighPriority = _isHighPriority.value,
                    isFocusEnabled = _isFocusEnabled.value
                )
                if (_isEditMode.value) {
                    todoRepository.updateTodo(todo.copy(id = todoRepository.getTodoById(todo.id)?.id ?: 0))
                } else {
                    todoRepository.insertTodo(todo)
                }
                resetState()
            }
            return true
        }
        else {
            return false
        }
    }

    // Xóa todo
    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
            resetState()
        }
    }

    // Reset state sau khi lưu/xóa
    private fun resetState() {
        _id.value = 0L
        _isEditMode.value = false
        _date.value = LocalDate.now()
        _startTime.value = LocalDateTime.now()
        _endTime.value = LocalDateTime.now().plusHours(1)
        _reminderOffsets.value = emptyList()
        _title.value = ""
        _detail.value = ""
        _isDone.value = false
        _isHighPriority.value = false
        _isFocusEnabled.value = false
        _isValid.value = true
        _errorMessage.value = null
    }

    // Xác thực đầu vào
    private fun validateInput(): Boolean {
        val isTitleValid = _title.value.isNotBlank()
        val isTimeValid = _startTime.value.isBefore(_endTime.value)
        _isValid.value = isTitleValid && isTimeValid
        _errorMessage.value = when {
            !isTitleValid -> "Tiêu đề không được để trống"
            !isTimeValid -> "Thời gian kết thúc phải sau thời gian bắt đầu"
            else -> null
        }
        return _isValid.value
    }
}