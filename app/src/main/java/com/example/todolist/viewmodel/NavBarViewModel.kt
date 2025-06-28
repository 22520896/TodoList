package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.ui.navbar.NavDes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor() : ViewModel() {

    /** các sự kiện UI 1-lần */
    sealed interface UiEvent {
        data object ShowAddTodoModal : UiEvent
        data object ShowAddEventModal : UiEvent
        data object ShowChooseModal : UiEvent
        data object NavigateToNewNote : UiEvent
    }

    private val _event = MutableSharedFlow<UiEvent>()
    val event: SharedFlow<UiEvent> = _event

    /** hàm được gọi khi FAB click, tùy route phát event */
    fun onFabClick(currentRoute: String?) {
        viewModelScope.launch {
            when (currentRoute) {
                NavDes.TODO.route       -> _event.emit(UiEvent.ShowAddTodoModal)
                NavDes.CALENDAR.route   -> _event.emit(UiEvent.ShowAddEventModal)
                NavDes.NOTE.route       -> _event.emit(UiEvent.NavigateToNewNote)
                NavDes.REPORT.route,
                NavDes.SETTING.route    -> _event.emit(UiEvent.ShowChooseModal)
                else                    -> _event.emit(UiEvent.ShowAddTodoModal)
            }
        }
    }

}


