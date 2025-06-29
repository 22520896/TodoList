package com.example.todolist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.home.HomeScreen
import com.example.todolist.ui.navbar.NavBar
import com.example.todolist.ui.navbar.NavGraph
import com.example.todolist.ui.theme.TodoListTheme
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.NavBarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListTheme {
                val navController = rememberNavController()
                val commonViewModel: CommonViewModel = hiltViewModel()
                NavBar(navController = navController, commonViewModel = commonViewModel) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        commonViewModel = commonViewModel
                    ) }
            }
        }
    }
}

