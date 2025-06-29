package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class CommonViewModel @Inject constructor(
) : ViewModel() {
    // State cho các trường nhập liệu
    private val _initDate = MutableStateFlow(LocalDate.now())
    val initDate = _initDate.asStateFlow()

    fun updateInitDate(date: LocalDate){
        _initDate.value = date
    }
}
