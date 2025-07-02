package com.example.todolist

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.navbar.NavBar
import com.example.todolist.ui.navbar.NavGraph
import com.example.todolist.ui.theme.TodoListTheme
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.NavBarViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.provider.Settings

//@AndroidEntryPoint
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // ✅ Yêu cầu quyền EXACT_ALARM nếu cần
//        requestExactAlarmPermissionIfNeeded()
//
//        enableEdgeToEdge()
//        setContent {
//            RequestNotificationPermission() // vẫn giữ POST_NOTIFICATIONS
//
//            TodoListTheme {
//                val navController = rememberNavController()
//                val commonViewModel: CommonViewModel = hiltViewModel()
//                val dateFormat by commonViewModel.dateFormat.collectAsStateWithLifecycle()
//                if (dateFormat == "") {
//                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                        Text("Loading...")
//                    }
//                } else {
//                    NavBar(navController = navController, commonViewModel = commonViewModel) { innerPadding ->
//                        NavGraph(
//                            navController = navController,
//                            commonViewModel = commonViewModel
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    private fun requestExactAlarmPermissionIfNeeded() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//            if (!alarmManager.canScheduleExactAlarms()) {
//                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
//                    data = Uri.parse("package:$packageName")
//                }
//                startActivity(intent)
//            }
//        }
//    }
//}
//
//@Composable
//fun RequestNotificationPermission() {
//    val context = LocalContext.current
//    val activity = context as? Activity
//
//    LaunchedEffect(Unit) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            val permission = android.Manifest.permission.POST_NOTIFICATIONS
//            val granted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
//
//            if (!granted && activity != null) {
//                ActivityCompat.requestPermissions(
//                    activity,
//                    arrayOf(permission),
//                    1001 // request code
//                )
//            }
//        }
//    }
//}
//

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        requestExactAlarmPermissionOnce()

        setContent {
            RequestNotificationPermission()
            TodoListTheme {
                val navController = rememberNavController()
                val commonViewModel: CommonViewModel = hiltViewModel()
                val dateFormat by commonViewModel.dateFormat.collectAsStateWithLifecycle()

                if (dateFormat.isBlank()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Loading...")
                    }
                } else {
                    NavBar(navController = navController, commonViewModel = commonViewModel) { _ ->
                        NavGraph(navController, commonViewModel)
                    }
                }
            }
        }
    }

    private fun requestExactAlarmPermissionOnce() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val alreadyChecked = prefs.getBoolean("exact_alarm_checked", false)

            if (!alarmManager.canScheduleExactAlarms() && !alreadyChecked) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
                prefs.edit().putBoolean("exact_alarm_checked", true).apply()
            }
        }
    }
}

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            val granted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

            if (!granted && activity != null) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(permission),
                    1001
                )
            }
        }
    }
}
