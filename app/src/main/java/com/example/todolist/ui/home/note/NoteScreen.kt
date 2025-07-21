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
import com.example.todolist.ui.home.note.NoteItemWithSwipe
import com.example.todolist.ui.navbar.NavDes
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.NoteViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NoteScreen(navController: NavController, viewModel: NoteViewModel = hiltViewModel(), commonViewModel: CommonViewModel, modifier: Modifier) {
    val dateFormat by commonViewModel.dateFormat.collectAsStateWithLifecycle()
    val color by commonViewModel.color.collectAsStateWithLifecycle()
    val notes by viewModel.allNotes.collectAsState(initial = emptyList())
    Box(modifier = Modifier.background(Color.White).fillMaxSize().                                                                    then(modifier)){
        LazyColumn (){
            items(notes) { note ->
                NoteItemWithSwipe(
                    note = note,
                    onDelete = { viewModel.delete(note) },
                    onFavouriteChange =  {checked ->
                        viewModel.updateNote(note.copy(isFavourite = checked))},
                    dateFormat = dateFormat,
                    color = color,
                    onClick = {
                        viewModel.startEditNote(note)
                        navController.navigate("${NavDes.NOTE_EDIT.route}/${note.id}")
                        {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                saveState = true
//                            }
//                            launchSingleTop = true
//                            restoreState = true
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


//@Composable
//fun NoteCardPreview(
//    title: String,
//    description: String,
//    time: LocalDate,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//    val formattedDate = time.format(formatter)
//
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable { onClick() }
//            .padding(16.dp),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(text = title, fontWeight = FontWeight.Bold)
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(text = description, color = Color.Gray)
//                }
//                Text(
//                    text = formattedDate,
//                    fontSize = 12.sp,
//                    textAlign = TextAlign.End
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun NoteItemWithSwipe(
//    note: Note,
//    onDelete: () -> Unit,
//    onClick: () -> Unit
//) {
//    val offsetX = remember { Animatable(0f, Float.VectorConverter) }
//    val scope = rememberCoroutineScope()
//    var showDelete by remember { mutableStateOf(false) }
//
//    val maxSwipe = with(LocalDensity.current) { 80.dp.toPx() } // Chiều rộng hiện icon xoá
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color.Transparent)
//            .pointerInput(Unit) {
//                detectHorizontalDragGestures(
//                    onDragEnd = {
//                        scope.launch {
//                            if (offsetX.value < -maxSwipe / 2) {
//                                offsetX.animateTo(-maxSwipe)
//                                showDelete = true
//                            } else {
//                                offsetX.animateTo(0f)
//                                showDelete = false
//                            }
//                        }
//                    },
//                    onHorizontalDrag = { _, dragAmount ->
//                        val newOffset = offsetX.value + dragAmount
//                        if (newOffset <= 0f) { // Chỉ cho kéo sang trái
//                            scope.launch {
//                                offsetX.snapTo(newOffset.coerceAtLeast(-maxSwipe))
//                            }
//                        }
//                    }
//                )
//            }
//    ) {
//        // Nền đỏ và icon thùng rác
//        if (showDelete) {
//            Box(
//                modifier = Modifier
//                    .matchParentSize()
//                    .background(Color.Red),
//                contentAlignment = Alignment.CenterEnd
//            ) {
//                IconButton(onClick = {
//                    onDelete()
//                    scope.launch {
//                        offsetX.snapTo(0f)
//                        showDelete = false
//                    }
//                }) {
//                    Icon(Icons.Default.Delete, contentDescription = "Xoá", tint = Color.White)
//                }
//            }
//        }
//
//        // Nội dung ghi chú (Note card)
//        NoteCardPreview(
//            title = note.title,
//            description = note.content,
//            time = note.updatedAt,
//            onClick = {
//                if (!showDelete) {
//                    onClick()
//                }
//            },
//            modifier = Modifier
//                .offset { IntOffset(offsetX.value.toInt(), 0) }
//        )
//    }
//}


