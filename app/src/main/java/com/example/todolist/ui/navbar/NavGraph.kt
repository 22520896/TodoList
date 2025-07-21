package com.example.todolist.ui.navbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.todolist.note.ui.NoteScreen
import com.example.todolist.ui.event.CalendarScreen
import com.example.todolist.ui.home.TopNavBar
import com.example.todolist.ui.home.note.NoteEditScreen
import com.example.todolist.ui.home.todo.TodoScreen
import com.example.todolist.ui.report.ReportScreen
import com.example.todolist.ui.setting.SettingScreen
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.EventViewModel
import com.example.todolist.viewmodel.NoteViewModel
import com.example.todolist.viewmodel.ReportViewModel
import com.example.todolist.viewmodel.TodoViewModel

//@Composable
//fun NavGraph(navController: NavHostController, commonViewModel: CommonViewModel) {
//    val reportViewModel: ReportViewModel = hiltViewModel()
//    val noteViewModel: NoteViewModel = hiltViewModel()
//    NavHost(
//        navController = navController,
//        startDestination = NavDes.HOME.route
//    ) {
//        composable(NavDes.HOME.route) { HomeScreen(navController, commonViewModel) }
//        composable(NavDes.CALENDAR.route) { CalendarScreen((navController)) }
//        composable(NavDes.REPORT.route) { ReportScreen(reportViewModel, commonViewModel) }
//        composable(NavDes.SETTING.route) { SettingScreen(commonViewModel) }
//        composable(NavDes.NOTE_EDIT.route) {
//            NoteEditScreen(navController = navController, viewModel = noteViewModel, noteId = null)
//        }
//    }
//}

@Composable
fun NavGraph(
    navController: NavHostController,
    commonViewModel: CommonViewModel,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val reportViewModel: ReportViewModel = hiltViewModel()
    val noteViewModel: NoteViewModel = hiltViewModel()
    val color by commonViewModel.color.collectAsStateWithLifecycle()
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
                        color = color
                    )
                },
                content = { innerPadding ->
                    TodoScreen(
                        commonViewModel = commonViewModel,
                        viewModel = todoViewModel,
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
                        color = color
                    )
                },
                content = { innerPadding ->
                    NoteScreen(
                        navController = navController,
                        commonViewModel = commonViewModel,
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
                commonViewModel = commonViewModel,
                noteId = null,
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
                commonViewModel = commonViewModel,
                noteId = id,
                modifier = Modifier.padding(paddingValues)
            )
        }

        composable(NavDes.CALENDAR.route) {
            CalendarScreen(commonViewModel = commonViewModel, modifier = Modifier.padding(paddingValues))
        }

        composable(NavDes.REPORT.route) {
            ReportScreen(reportViewModel, commonViewModel)
        }

        composable(NavDes.SETTING.route) {
            SettingScreen(commonViewModel)
        }
    }
}



