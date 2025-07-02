package com.example.todolist.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import com.example.todolist.ui.home.todo.HorizontalCalendar
import com.example.todolist.ui.home.todo.TodoScreen
import com.example.todolist.ui.navbar.NavBar
import java.time.LocalDate
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todolist.data.entity.Note
import com.example.todolist.note.ui.NoteScreen
import com.example.todolist.ui.home.note.NoteEditScreen
import com.example.todolist.ui.navbar.NavDes
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.NoteViewModel

//@Composable
//fun HomeScreen(navController: NavController, commonViewModel: CommonViewModel = hiltViewModel()){
//    val childNav = rememberNavController()
//    val noteViewModel: NoteViewModel = hiltViewModel()
//    val color by commonViewModel.color.collectAsStateWithLifecycle()
//    Column  {
//        TopNavBar(navController = childNav, color = color)
//        NavHost(
//            navController = childNav,
//            startDestination = NavDes.TODO.route,
//            modifier = Modifier.fillMaxSize()
//        ) {
//            composable(NavDes.TODO.route) { TodoScreen(commonViewModel= commonViewModel) }
//            composable(NavDes.NOTE.route) { NoteScreen(childNav) }
//            composable(
//                "${NavDes.NOTE_EDIT.route}/{id}",
//                arguments = listOf(
//                    navArgument("id") {
//                        type = NavType.LongType
//                    }
//                )
//            ) { backStackEntry ->
//                val id = backStackEntry.arguments?.getLong("id")
//                NoteEditScreen(navController = navController, viewModel = noteViewModel, noteId = id?:0)
//            }
//        }
//    }
//}


// Điều hướng Todo/Note
@Composable
fun TopNavBar(navController: NavController, color: String) {
    val items = listOf(NavDes.TODO, NavDes.NOTE)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: NavDes.TODO.route

    TopAppBar(
        backgroundColor = Color.White,
        elevation = 0.dp,
        contentPadding = PaddingValues(top = 0.dp),
        modifier = Modifier.safeDrawingPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Row(
                modifier = Modifier
                    .width(200.dp)
                    .height(45.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFF2F2F2))
                    .padding(4.dp), // Padding khung
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    val selected = currentRoute == item.route

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (selected) Color(android.graphics.Color.parseColor(color)) else Color.Transparent
                            )
                            .height(33.dp)
                            .clickable {
                                if (!selected) navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.label,
                            color = if (selected) Color.White else Color.Black,
                            fontSize = 14.sp
                        )
                    }

                    if (index == 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Divider(
                            modifier = Modifier
                                .height(30.dp)
                                .width(2.dp),
                            color = Color(0xFFE0E0E0)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

