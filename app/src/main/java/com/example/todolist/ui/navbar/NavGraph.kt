package com.example.todolist.ui.navbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolist.note.ui.NoteScreen
import com.example.todolist.ui.TopNavBar
import com.example.todolist.ui.event.CalendarScreen
import com.example.todolist.ui.note.NoteEditScreen
import com.example.todolist.ui.todo.TodoScreen
import com.example.todolist.ui.report.ReportScreen
import com.example.todolist.ui.setting.SettingScreen
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.NoteViewModel
import com.example.todolist.viewmodel.ReportViewModel
import com.example.todolist.viewmodel.TodoViewModel
import java.time.format.DateTimeFormatter


@Composable
fun NavGraph(
    navController: NavHostController,
    commonViewModel: CommonViewModel,
    formatterDate: DateTimeFormatter,
    formatterTime: DateTimeFormatter,
    bgColor: Color,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val reportViewModel: ReportViewModel = hiltViewModel()
    val noteViewModel: NoteViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = NavDes.TODO.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(NavDes.TODO.route) {
            val todoViewModel: TodoViewModel = hiltViewModel()
            Scaffold(
                topBar = {
                    TopNavBar(
                        navController = navController,
                        color = bgColor
                    )
                },
                content = { innerPadding ->
                    TodoScreen(
                        commonViewModel = commonViewModel,
                        viewModel = todoViewModel,
                        formatterDate = formatterDate,
                        formatterTime = formatterTime,
                        bgColor = bgColor,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(paddingValues)
                    )
                }
            )
        }

        composable(NavDes.NOTE.route) {
            Scaffold(
                topBar = {
                    TopNavBar(
                        navController = navController,
                        color = bgColor
                    )
                },
                content = { innerPadding ->
                    NoteScreen(
                        navController = navController,
                        formatterDate = formatterDate,
                        color = bgColor,
                        viewModel = noteViewModel,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(paddingValues)
                    )
                }
            )
        }

        composable(NavDes.NOTE_EDIT.route) {
            NoteEditScreen(
                navController = navController,
                viewModel = noteViewModel,
                noteId = null,
                color = bgColor,
                modifier = Modifier.padding(paddingValues)
            )
        }

        composable(
            "${NavDes.NOTE_EDIT.route}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType; defaultValue = 0 }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            NoteEditScreen(
                navController = navController,
                viewModel = noteViewModel,
                noteId = id,
                color = bgColor,
                modifier = Modifier.padding(paddingValues)
            )
        }

        composable(NavDes.CALENDAR.route) {
            CalendarScreen(
                formatterDate = formatterDate,
                formatterTime = formatterTime,
                color = bgColor,
                modifier = Modifier.padding(paddingValues)
            )
        }

        composable(NavDes.REPORT.route) {
            ReportScreen(
                reportViewModel = reportViewModel,
                color = bgColor
            )
        }

        composable(NavDes.SETTING.route) {
            SettingScreen(commonViewModel)
        }
    }
}



