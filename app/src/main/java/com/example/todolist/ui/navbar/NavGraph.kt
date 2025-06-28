package com.example.todolist.ui.navbar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todolist.ui.calendar.CalendarScreen
import com.example.todolist.ui.home.HomeScreen
import com.example.todolist.ui.report.ReportScreen
import com.example.todolist.ui.setting.SettingScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavDes.HOME.route
    ) {
        composable(NavDes.HOME.route) { HomeScreen(navController) }
        composable(NavDes.CALENDAR.route) { CalendarScreen((navController)) }
        composable(NavDes.ADD.route) { /* Placeholder cho AddScreen */ }
        composable(NavDes.REPORT.route) { ReportScreen(navController) }
        composable(NavDes.SETTING.route) { SettingScreen(navController) }
    }
}

