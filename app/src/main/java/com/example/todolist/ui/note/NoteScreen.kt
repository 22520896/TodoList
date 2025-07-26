package com.example.todolist.note.ui
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.todolist.data.entity.Note
import com.example.todolist.ui.note.NoteItemWithSwipe
import com.example.todolist.ui.navbar.NavDes
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.NoteViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NoteScreen(navController: NavController,
               viewModel: NoteViewModel = hiltViewModel(),
               formatterDate: DateTimeFormatter,
               color: Color,
               modifier: Modifier)
{
    val notes by viewModel.allNotes.collectAsState(initial = emptyList())
    Box(modifier = Modifier.background(Color.White).fillMaxSize().then(modifier)){
        LazyColumn (){
            items(notes) { note ->
                NoteItemWithSwipe(
                    note = note,
                    onDelete = { viewModel.delete(note) },
                    onFavouriteChange =  {checked ->
                        viewModel.updateNote(note.copy(isFavourite = checked))},
                    formatterDate = formatterDate,
                    color = color,
                    onClick = {
                        viewModel.startEditNote(note)
                        navController.navigate("${NavDes.NOTE_EDIT.route}/${note.id}")
                        {
                            popUpTo(NavDes.NOTE.route) { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

}




