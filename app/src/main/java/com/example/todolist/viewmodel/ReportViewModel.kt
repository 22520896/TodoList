package com.example.todolist.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entity.Report
import com.example.todolist.data.entity.Todo
import com.example.todolist.data.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@HiltViewModel
class ReportViewModel @Inject constructor(private val repository: ReportRepository) : ViewModel() {

    private val _report = MutableStateFlow<Report?>(null)
    val report = _report.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    fun setDate(date: LocalDate) {
        _selectedDate.value = date
        loadReport(date)
    }

    fun loadReport(date: LocalDate){
        val yearMonth = date.format(DateTimeFormatter.ofPattern("yyyy-MM"))
        viewModelScope.launch {
            repository.getReports(yearMonth).collect { report ->
                _report.value = report
            }
        }
    }

    init {
        loadReport(_selectedDate.value)
    }
}
