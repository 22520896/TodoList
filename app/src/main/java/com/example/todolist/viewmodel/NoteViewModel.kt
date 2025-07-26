package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.entity.Note
import com.example.todolist.data.repository.NoteRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    // Danh sách tất cả ghi chú
    val allNotes: StateFlow<List<Note>> = repository.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // State cho chế độ chỉnh sửa hay thêm mới
    private val _isEditMode = MutableStateFlow(false)
    val isEditMode = _isEditMode.asStateFlow()

    private val _id = MutableStateFlow(0L)
    val id = _id.asStateFlow()

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
    }

    fun startAddNote() {
        _isEditMode.value = false
        _id.value = 0
        _title.value = ""
        _content.value = ""
    }

    fun startEditNote(note: Note) {
        _isEditMode.value = true
        _id.value = note.id
        _title.value = note.title
        _content.value = note.content
    }

    fun saveNote() {
        viewModelScope.launch {
            val note = Note(
                id = if (_isEditMode.value) _id.value else 0L,
                title = _title.value,
                content = _content.value,
                updatedAt = LocalDate.now()
            )
            if (_isEditMode.value) {
                repository.update(note)
            } else {
                repository.insert(note)
            }
            resetState()
        }
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
        resetState()
    }

    fun updateNote(note: Note){
        viewModelScope.launch {
            repository.update(note)
        }
    }

    private fun resetState() {
        _id.value = 0
        _title.value = ""
        _content.value = ""
        _isEditMode.value = false
    }
}