package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entity.Setting
import com.example.todolist.data.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


//@HiltViewModel
//class CommonViewModel @Inject constructor( private val repository: SettingRepository ) : ViewModel() {
//    private val _initDate = MutableStateFlow(LocalDate.now())
//    val initDate = _initDate.asStateFlow()
//
//    private val _dateFormat = MutableStateFlow("")
//    val dateFormat = _dateFormat.asStateFlow()
//
//    private val _timeFormat = MutableStateFlow("")
//    val timeFormat = _timeFormat.asStateFlow()
//
//    private val _color = MutableStateFlow("#FFFFFF")
//    val color = _color.asStateFlow()
//
//    private val _ringtoneName = MutableStateFlow("")
//    val ringtoneName = _ringtoneName.asStateFlow()
//
//    private val _ringtoneUri = MutableStateFlow("")
//    val ringtoneUri = _ringtoneUri.asStateFlow()
//
//
//    fun updateInitDate(date: LocalDate){
//        _initDate.value = date
//    }
//
//    fun loadSetting() {
//        viewModelScope.launch {
//            repository.getSetting().collect { setting ->
//                _dateFormat.value = setting.dateFormat
//                _timeFormat.value = setting.timeFormat
//                _color.value = setting.color
//                _ringtoneName.value = setting.ringtoneName
//                _ringtoneUri.value = setting.ringtoneName
//            }
//        }
//    }
//
//    fun updateDateFormat(format: String) {
//        viewModelScope.launch {
//            repository.updateDateFormat(format)
//            _dateFormat.value = format
//        }
//
//    }
//
//    fun updateTimeFormat(format: String) {
//        viewModelScope.launch {
//            repository.updateTimeFormat(format)
//            _timeFormat.value = format
//        }
//    }
//
//    fun updateColor(color: String) {
//        viewModelScope.launch {
//            repository.updateColor(color)
//            _color.value = color
//        }
//    }
//
//    fun updateRingtone(uri: String, name: String) {
//        viewModelScope.launch {
//            repository.updateRingtone(uri, name)
//            _ringtoneUri.value = uri
//            _ringtoneName.value = name
//        }
//    }
//
//    init {
//        loadSetting()
//    }
//}

@HiltViewModel
class CommonViewModel @Inject constructor(private val repository: SettingRepository) : ViewModel() {
    private val _initDate = MutableStateFlow(LocalDate.now())
    val initDate = _initDate.asStateFlow()

    private val _dateFormat = MutableStateFlow("")
    val dateFormat = _dateFormat.asStateFlow()

    private val _timeFormat = MutableStateFlow("")
    val timeFormat = _timeFormat.asStateFlow()

    private val _color = MutableStateFlow("#FFFFFF")
    val color = _color.asStateFlow()

    private val _ringtoneName = MutableStateFlow("")
    val ringtoneName = _ringtoneName.asStateFlow()

    private val _ringtoneUri = MutableStateFlow("")
    val ringtoneUri = _ringtoneUri.asStateFlow()

    fun updateInitDate(date: LocalDate) {
        _initDate.value = date
    }

    fun loadSetting() {
        viewModelScope.launch {
            repository.getSetting().collect { setting ->
                _dateFormat.value = setting.dateFormat
                _timeFormat.value = setting.timeFormat
                _color.value = setting.color
            }
        }
        // Ringtone lấy trực tiếp từ SharedPreferences
        _ringtoneUri.value = repository.getRingtoneUri()
        _ringtoneName.value = repository.getRingtoneName()
    }

    fun updateDateFormat(format: String) {
        viewModelScope.launch {
            repository.updateDateFormat(format)
            _dateFormat.value = format
        }
    }

    fun updateTimeFormat(format: String) {
        viewModelScope.launch {
            repository.updateTimeFormat(format)
            _timeFormat.value = format
        }
    }

    fun updateColor(color: String) {
        viewModelScope.launch {
            repository.updateColor(color)
            _color.value = color
        }
    }

    fun updateRingtone(uri: String, name: String) {
        viewModelScope.launch {
            repository.saveRingtone(uri, name)
        }
        _ringtoneUri.value = uri
        _ringtoneName.value = name
    }

    init {
        loadSetting()
    }
}
